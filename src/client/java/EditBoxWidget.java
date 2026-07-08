import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.AbstractTextAreaWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Util;
import tokenizer.Type;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reimplementation of {@link net.minecraft.client.gui.components.MultiLineEditBox}
 */
public class EditBoxWidget extends AbstractTextAreaWidget {
    private static final int UNFOCUSED_BOX_TEXT_COLOR = 0xCCE0E0E0;
    private static final int CURSOR_BLINK_INTERVAL = 500;
    private static final int LINE_HEIGHT = 11;
    private final Font textRenderer;
    private final Component placeholder;
    private final EditBox editBox;
    private long lastSwitchFocusTime = Util.getMillis();

    EditBoxWidget(final Font textRenderer, int x, int y, int width, int height, Component placeholder, Component message, boolean hasBackground, boolean hasOverlay) {
        Objects.requireNonNull(textRenderer);
        super(x, y, width, height, message, AbstractScrollArea.defaultSettings((int) ((double) 9.0F / (double) 2.0F)), hasBackground, hasOverlay);
        this.textRenderer = textRenderer;
        this.placeholder = placeholder;
        this.editBox = new EditBox(textRenderer, width - this.totalInnerPadding(), LINE_HEIGHT);
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

    public String getText() {
        return this.editBox.getText();
    }

    public void setText(String text) {
        this.setText(text, false);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, Component.translatable("gui.narrate.editBox", this.getMessage(), this.getText()));
    }

    @Override
    public void onClick(MouseButtonEvent click, boolean doubled) {
        if (doubled) {
            this.editBox.selectWord();
        } else {
            this.editBox.setSelecting(click.hasShiftDown());
            this.moveCursor(click.x(), click.y());
        }

    }

    @Override
    protected void onDrag(MouseButtonEvent click, double offsetX, double offsetY) {
        this.editBox.setSelecting(true);
        this.moveCursor(click.x(), click.y());
        this.editBox.setSelecting(click.hasShiftDown());
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        return this.editBox.handleSpecialKey(input);
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
        if (this.visible && this.isFocused() && input.isAllowedChatCharacter()) {
            this.editBox.replaceSelection(input.codepointAsString());
            return true;
        } else {
            return false;
        }
    }

    private void drawText(GuiGraphicsExtractor context, String text, int x, int y) {
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

                    context.text(textRenderer, group, x, y, color, false);
                    position += group.length();
                    x += textRenderer.width(group);
                    error = false;
                    break;
                }
            }

            if (error) {
                context.text(textRenderer, text.substring(position), x, y, 0xFFF75464, false);
                break;
            }
        }
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(getX(), getY(), getX() + getWidth(), getY() + Math.max(getHeight(), editBox.getLineCount() * LINE_HEIGHT + 6), 0xFF1E1F22);
        String text = editBox.getText();

        if (text.isEmpty() && !isFocused()) {
            context.textWithWordWrap(textRenderer, placeholder, getInnerLeft(), getInnerTop() + 2, width - totalInnerPadding(), UNFOCUSED_BOX_TEXT_COLOR);
            return;
        }

        int pos = editBox.getCursor();
        boolean focused = isFocused() && (Util.getMillis() - lastSwitchFocusTime) / CURSOR_BLINK_INTERVAL % 2L == 0L;
        int x;
        int textY = getInnerTop() + 2;

        for (EditBox.Substring line : editBox.getLines()) {
            boolean isVisible = withinContentAreaTopBottom(textY, textY + LINE_HEIGHT);
            int textX = getInnerLeft();

            if (isVisible) {
                String fullLineText = text.substring(line.beginIndex(), line.endIndex());
                drawText(context, fullLineText, textX, textY);
            }

            if (focused && pos >= line.beginIndex() && pos <= line.endIndex()) {
                if (isVisible) {
                    String lineUntilPos = text.substring(line.beginIndex(), pos);
                    x = textX + textRenderer.width(lineUntilPos);
                    context.fill(x, textY - 2, x + 1, textY + 9, 0xE0FFFFFF);
                }
            }

            textY += LINE_HEIGHT;
        }

        if (editBox.hasSelection()) {
            EditBox.Substring substring2 = editBox.getSelection();
            int n = getInnerLeft();
            textY = getInnerTop();

            for (EditBox.Substring substring3 : editBox.getLines()) {
                if (substring2.beginIndex() <= substring3.endIndex()) {
                    if (substring3.beginIndex() > substring2.endIndex()) {
                        break;
                    }

                    if (withinContentAreaTopBottom(textY, textY + LINE_HEIGHT)) {
                        int o = textRenderer.width(text.substring(substring3.beginIndex(), Math.max(substring2.beginIndex(), substring3.beginIndex())));
                        int p;

                        if (substring2.endIndex() > substring3.endIndex()) {
                            p = width - innerPadding();
                        } else {
                            p = textRenderer.width(text.substring(substring3.beginIndex(), substring2.endIndex()));
                        }

                        context.textHighlight(n + o, textY, n + p, textY + LINE_HEIGHT, true);
                    }
                }

                textY += LINE_HEIGHT;
            }
        }
    }

    @Override
    protected void extractDecorations(GuiGraphicsExtractor context) {
        super.extractDecorations(context);
        if (this.editBox.hasMaxLength()) {
            int i = this.editBox.getMaxLength();
            Component text = Component.translatable("gui.multiLineEditBox.character_limit", this.editBox.getText().length(), i);
            context.text(this.textRenderer, text, this.getX() + this.width - this.textRenderer.width(text), this.getY() + this.height + 4, CommonColors.LIGHT_GRAY);
        }
    }

    @Override
    public int getInnerHeight() {
        return LINE_HEIGHT * this.editBox.getLineCount();
    }

    @Override
    protected double scrollRate() {
        return LINE_HEIGHT * 3.0;
    }

    private void onCursorChange() {
        double d = this.scrollAmount();
        EditBox.Substring substring = this.editBox.getLine((int) (d / LINE_HEIGHT));
        if (this.editBox.getCursor() <= substring.beginIndex()) {
            d = this.editBox.getCurrentLineIndex() * LINE_HEIGHT;
        } else {
            EditBox.Substring substring2 = this.editBox.getLine((int) ((d + this.height) / LINE_HEIGHT) - 1);
            if (this.editBox.getCursor() > substring2.endIndex()) {
                d = this.editBox.getCurrentLineIndex() * LINE_HEIGHT - this.height + LINE_HEIGHT + this.totalInnerPadding();
            }
        }

        this.setScrollAmount(d);
    }

    private void moveCursor(double mouseX, double mouseY) {
        double d = mouseX - this.getX() - this.innerPadding();
        double e = mouseY - this.getY() - this.innerPadding() + this.scrollAmount();
        this.editBox.moveCursor(d, e);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (focused) {
            this.lastSwitchFocusTime = Util.getMillis();
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

        public EditBoxWidget build(Font textRenderer, int width, int height, Component message) {
            boolean hasBackground = true;
            boolean hasOverlay = true;
            return new EditBoxWidget(textRenderer, this.x, this.y, width, height, CommonComponents.EMPTY, message, hasBackground, hasOverlay);
        }
    }
}
