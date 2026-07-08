import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ScrollableTextFieldWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Util;
import tokenizer.Type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reimplementation of {@link net.minecraft.client.gui.widget.EditBoxWidget}
 */
public class EditBoxWidget extends ScrollableTextFieldWidget {
    private static final int UNFOCUSED_BOX_TEXT_COLOR = 0xCCE0E0E0;
    private static final int CURSOR_BLINK_INTERVAL = 500;
    private static final int LINE_HEIGHT = 11;
    private final TextRenderer textRenderer;
    /**
     * The placeholder text that gets rendered when the edit box is empty. This does not
     * get returned from {@link #getText}; an empty text will be returned in such cases.
     */
    private final Text placeholder;
    private final EditBox editBox;
    private long lastSwitchFocusTime = Util.getMeasuringTimeMs();

    EditBoxWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text placeholder, Text message, boolean hasBackground, boolean hasOverlay) {
        super(x, y, width, height, message, hasBackground, hasOverlay);
        this.textRenderer = textRenderer;
        this.placeholder = placeholder;
        this.editBox = new EditBox(textRenderer, width - this.getPadding(), LINE_HEIGHT);
        this.editBox.setCursorChangeListener(this::onCursorChange);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public void setText(String text, boolean allowOverflow) {
        this.editBox.setText(text, allowOverflow);
    }

    /**
     * {@return the current text of the edit box}
     */
    public String getText() {
        return this.editBox.getText();
    }

    /**
     * Sets the text of the edit box and moves the cursor to the end of the edit box.
     */
    public void setText(String text) {
        this.setText(text, false);
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, Text.translatable("gui.narrate.editBox", this.getMessage(), this.getText()));
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (doubled) {
            this.editBox.selectWord();
        } else {
            this.editBox.setSelecting(click.hasShift());
            this.moveCursor(click.x(), click.y());
        }

    }

    @Override
    protected void onDrag(Click click, double offsetX, double offsetY) {
        this.editBox.setSelecting(true);
        this.moveCursor(click.x(), click.y());
        this.editBox.setSelecting(click.hasShift());
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        return this.editBox.handleSpecialKey(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (this.visible && this.isFocused() && input.isValidChar()) {
            this.editBox.replaceSelection(input.asString());
            return true;
        } else {
            return false;
        }
    }

    private void drawText(DrawContext context, String text, int x, int y) {
        int position = 0;

        while (position < text.length()) {
            boolean error = true;

            for (Type type : Type.getEntries()) {
                Matcher matcher = Pattern.compile("^" + type.getRegex()).matcher(text.substring(position) + "\n");

                if (matcher.find()) {
                    String group = type == Type.QUOTE ? matcher.group(0) : matcher.group(1);

                    int color = switch (type) {
                        case Type.QUOTE -> 0xFF6AAB73;
                        case Type.KEYWORD, Type.BOOLEAN, Type.BOOLEAN_OPERATOR, Type.NULL -> 0xFFCF8E6D;
                        case Type.INTEGER, Type.FLOAT -> 0xFF2AACB8;
                        case Type.COMMENT -> 0xFF7A7E85;
                        default -> 0xFFBCBEC4;
                    };

                    if (type == Type.IDENTIFIER && position + group.length() < text.length() && text.charAt(position + group.length()) == '(') {
                        color = 0xFFC77DBB;
                    }

                    context.drawText(textRenderer, group, x, y, color, false);
                    position += group.length();
                    x += textRenderer.getWidth(group);
                    error = false;
                    break;
                }
            }

            if (error) {
                context.drawText(textRenderer, text.substring(position), x, y, 0xFFF75464, false);
                break;
            }
        }
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(getX(), getY(), getX() + getWidth(), getY() + Math.max(getHeight(), editBox.getLineCount() * LINE_HEIGHT + 6), 0xFF1E1F22);
        String text = editBox.getText();

        if (text.isEmpty() && !isFocused()) {
            context.drawWrappedTextWithShadow(textRenderer, placeholder, getTextX(), getTextY() + 2, width - getPadding(), UNFOCUSED_BOX_TEXT_COLOR);
            return;
        }

        int pos = editBox.getCursor();
        boolean focused = isFocused() && (Util.getMeasuringTimeMs() - lastSwitchFocusTime) / CURSOR_BLINK_INTERVAL % 2L == 0L;
        int x;
        int textY = getTextY() + 2;

        for (EditBox.Substring line : editBox.getLines()) {
            boolean isVisible = isVisible(textY, textY + LINE_HEIGHT);
            int textX = getTextX();

            if (isVisible) {
                String fullLineText = text.substring(line.beginIndex(), line.endIndex());
                drawText(context, fullLineText, textX, textY);
            }

            if (focused && pos >= line.beginIndex() && pos <= line.endIndex()) {
                if (isVisible) {
                    String lineUntilPos = text.substring(line.beginIndex(), pos);
                    x = textX + textRenderer.getWidth(lineUntilPos);
                    context.fill(x, textY - 2, x + 1, textY + 9, 0xE0FFFFFF);
                }
            }

            textY += LINE_HEIGHT;
        }

        if (editBox.hasSelection()) {
            EditBox.Substring substring2 = editBox.getSelection();
            int n = getTextX();
            textY = getTextY();

            for (EditBox.Substring substring3 : editBox.getLines()) {
                if (substring2.beginIndex() <= substring3.endIndex()) {
                    if (substring3.beginIndex() > substring2.endIndex()) {
                        break;
                    }

                    if (isVisible(textY, textY + LINE_HEIGHT)) {
                        int o = textRenderer.getWidth(text.substring(substring3.beginIndex(), Math.max(substring2.beginIndex(), substring3.beginIndex())));
                        int p;

                        if (substring2.endIndex() > substring3.endIndex()) {
                            p = width - getTextMargin();
                        } else {
                            p = textRenderer.getWidth(text.substring(substring3.beginIndex(), substring2.endIndex()));
                        }

                        context.drawSelection(n + o, textY, n + p, textY + LINE_HEIGHT, true);
                    }
                }

                textY += LINE_HEIGHT;
            }
        }
    }

    @Override
    protected void renderOverlay(DrawContext context) {
        super.renderOverlay(context);
        if (this.editBox.hasMaxLength()) {
            int i = this.editBox.getMaxLength();
            Text text = Text.translatable("gui.multiLineEditBox.character_limit", this.editBox.getText().length(), i);
            context.drawTextWithShadow(this.textRenderer, text, this.getX() + this.width - this.textRenderer.getWidth(text), this.getY() + this.height + 4, Colors.LIGHT_GRAY);
        }
    }

    @Override
    public int getContentsHeight() {
        return LINE_HEIGHT * this.editBox.getLineCount();
    }

    @Override
    protected double getDeltaYPerScroll() {
        return LINE_HEIGHT * 3.0;
    }

    private void onCursorChange() {
        double d = this.getScrollY();
        EditBox.Substring substring = this.editBox.getLine((int) (d / LINE_HEIGHT));
        if (this.editBox.getCursor() <= substring.beginIndex()) {
            d = this.editBox.getCurrentLineIndex() * LINE_HEIGHT;
        } else {
            EditBox.Substring substring2 = this.editBox.getLine((int) ((d + this.height) / LINE_HEIGHT) - 1);
            if (this.editBox.getCursor() > substring2.endIndex()) {
                d = this.editBox.getCurrentLineIndex() * LINE_HEIGHT - this.height + LINE_HEIGHT + this.getPadding();
            }
        }

        this.setScrollY(d);
    }

    private void moveCursor(double mouseX, double mouseY) {
        double d = mouseX - this.getX() - this.getTextMargin();
        double e = mouseY - this.getY() - this.getTextMargin() + this.getScrollY();
        this.editBox.moveCursor(d, e);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (focused) {
            this.lastSwitchFocusTime = Util.getMeasuringTimeMs();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private int x;
        private int y;

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public EditBoxWidget build(TextRenderer textRenderer, int width, int height, Text message) {
            boolean hasBackground = true;
            boolean hasOverlay = true;
            return new EditBoxWidget(textRenderer, this.x, this.y, width, height, ScreenTexts.EMPTY, message, hasBackground, hasOverlay);
        }
    }
}
