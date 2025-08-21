package com.braydenoneal;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ScrollableTextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;

import java.util.function.Consumer;

public class EditBoxWidget extends ScrollableTextFieldWidget {
    private static final int CURSOR_PADDING = 1;
    private static final int CURSOR_COLOR = -3092272;
    private static final String UNDERSCORE = "_";
    private static final int FOCUSED_BOX_TEXT_COLOR = -2039584;
    private static final int UNFOCUSED_BOX_TEXT_COLOR = -857677600;
    private static final int CURSOR_BLINK_INTERVAL = 300;
    private final TextRenderer textRenderer;
    /**
     * The placeholder text that gets rendered when the edit box is empty. This does not
     * get returned from {@link #getText}; an empty text will be returned in such cases.
     */
    private final Text placeholder;
    private final EditBox editBox;
    private final int textColor;
    private final boolean textShadow;
    private final int cursorColor;
    private long lastSwitchFocusTime = Util.getMeasuringTimeMs();

    EditBoxWidget(
            TextRenderer textRenderer,
            int x,
            int y,
            int width,
            int height,
            Text placeholder,
            Text message,
            int textColor,
            boolean textShadow,
            int cursorColor,
            boolean hasBackground,
            boolean hasOverlay
    ) {
        super(x, y, width, height, message, hasBackground, hasOverlay);
        this.textRenderer = textRenderer;
        this.textShadow = textShadow;
        this.textColor = textColor;
        this.cursorColor = cursorColor;
        this.placeholder = placeholder;
        this.editBox = new EditBox(textRenderer, width - this.getPadding());
        this.editBox.setCursorChangeListener(this::onCursorChange);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    /**
     * Sets the maximum length of the edit box text in characters.
     *
     * <p>If {@code maxLength} equals {@link EditBox#UNLIMITED_LENGTH}, the edit box does not
     * have a length limit, and the widget does not show the current text length indicator.
     *
     * @throws IllegalArgumentException if {@code maxLength} is negative
     * @see EditBox#setMaxLength
     */
    public void setMaxLength(int maxLength) {
        this.editBox.setMaxLength(maxLength);
    }

    public void setMaxLines(int maxLines) {
        this.editBox.setMaxLines(maxLines);
    }

    /**
     * Sets the change listener that is called every time the text changes.
     *
     * @param changeListener the listener that takes the new text of the edit box
     */
    public void setChangeListener(Consumer<String> changeListener) {
        this.editBox.setChangeListener(changeListener);
    }

    /**
     * Sets the text of the edit box and moves the cursor to the end of the edit box.
     */
    public void setText(String text) {
        this.setText(text, false);
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

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, Text.translatable("gui.narrate.editBox", new Object[]{this.getMessage(), this.getText()}));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.editBox.setSelecting(Screen.hasShiftDown());
        this.moveCursor(mouseX, mouseY);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.editBox.setSelecting(true);
        this.moveCursor(mouseX, mouseY);
        this.editBox.setSelecting(Screen.hasShiftDown());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.editBox.handleSpecialKey(keyCode);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.visible && this.isFocused() && StringHelper.isValidChar(chr)) {
            this.editBox.replaceSelection(Character.toString(chr));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        String text = this.editBox.getText();
        if (text.isEmpty() && !this.isFocused()) {
            context.drawWrappedTextWithShadow(this.textRenderer, this.placeholder, this.getTextX(), this.getTextY(), this.width - this.getPadding(), -857677600);
        } else {
            int pos = this.editBox.getCursor();
            boolean focused = this.isFocused() && (Util.getMeasuringTimeMs() - this.lastSwitchFocusTime) / 500L % 2L == 0L;
            int x;
            int textY = this.getTextY();

            for (EditBox.Substring line : this.editBox.getLines()) {
                boolean isVisible = this.isVisible(textY, textY + 9);
                int textX = this.getTextX();
                if (focused && pos >= line.beginIndex() && pos <= line.endIndex()) {
                    if (isVisible) {
                        String lineUntilPos = text.substring(line.beginIndex(), pos);
                        context.drawText(this.textRenderer, lineUntilPos, textX, textY, this.textColor, this.textShadow);
                        x = textX + this.textRenderer.getWidth(lineUntilPos);
                        context.fill(x, textY - 1, x + 1, textY + 1 + 9, this.cursorColor);
                        context.drawText(this.textRenderer, text.substring(pos, line.endIndex()), x, textY, this.textColor, this.textShadow);
                    }
                } else {
                    if (isVisible) {
                        String fullLineText = text.substring(line.beginIndex(), line.endIndex());
                        context.drawText(this.textRenderer, fullLineText, textX, textY, this.textColor, this.textShadow);
                    }
                }

                textY += 9;
            }

            if (this.editBox.hasSelection()) {
                EditBox.Substring substring2 = this.editBox.getSelection();
                int n = this.getTextX();
                textY = this.getTextY();

                for (EditBox.Substring substring3 : this.editBox.getLines()) {
                    if (substring2.beginIndex() > substring3.endIndex()) {
                        textY += 9;
                    } else {
                        if (substring3.beginIndex() > substring2.endIndex()) {
                            break;
                        }

                        if (this.isVisible(textY, textY + 9)) {
                            int o = this.textRenderer.getWidth(text.substring(substring3.beginIndex(), Math.max(substring2.beginIndex(), substring3.beginIndex())));
                            int p;
                            if (substring2.endIndex() > substring3.endIndex()) {
                                p = this.width - this.getTextMargin();
                            } else {
                                p = this.textRenderer.getWidth(text.substring(substring3.beginIndex(), substring2.endIndex()));
                            }

                            context.drawSelection(n + o, textY, n + p, textY + 9);
                        }

                        textY += 9;
                    }
                }
            }
        }
    }

    @Override
    protected void renderOverlay(DrawContext context) {
        super.renderOverlay(context);
        if (this.editBox.hasMaxLength()) {
            int i = this.editBox.getMaxLength();
            Text text = Text.translatable("gui.multiLineEditBox.character_limit", new Object[]{this.editBox.getText().length(), i}).getWithStyle(Style.EMPTY.withFont(Identifier.ofVanilla("uniform"))).getFirst();
            context.drawTextWithShadow(
                    this.textRenderer, text, this.getX() + this.width - this.textRenderer.getWidth(text), this.getY() + this.height + 4, Colors.LIGHT_GRAY
            );
        }
    }

    @Override
    public int getContentsHeight() {
        return 9 * this.editBox.getLineCount();
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 9.0 / 2.0;
    }

    private void onCursorChange() {
        double d = this.getScrollY();
        EditBox.Substring substring = this.editBox.getLine((int) (d / 9.0));
        if (this.editBox.getCursor() <= substring.beginIndex()) {
            d = this.editBox.getCurrentLineIndex() * 9;
        } else {
            EditBox.Substring substring2 = this.editBox.getLine((int) ((d + this.height) / 9.0) - 1);
            if (this.editBox.getCursor() > substring2.endIndex()) {
                d = this.editBox.getCurrentLineIndex() * 9 - this.height + 9 + this.getPadding();
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

    public static EditBoxWidget.Builder builder() {
        return new EditBoxWidget.Builder();
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private int x;
        private int y;
        private Text placeholder = ScreenTexts.EMPTY;
        private int textColor = -2039584;
        private boolean textShadow = true;
        private int cursorColor = -3092272;
        private boolean hasBackground = true;
        private boolean hasOverlay = true;

        public EditBoxWidget.Builder x(int x) {
            this.x = x;
            return this;
        }

        public EditBoxWidget.Builder y(int y) {
            this.y = y;
            return this;
        }

        public EditBoxWidget.Builder placeholder(Text placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public EditBoxWidget.Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public EditBoxWidget.Builder textShadow(boolean textShadow) {
            this.textShadow = textShadow;
            return this;
        }

        public EditBoxWidget.Builder cursorColor(int cursorColor) {
            this.cursorColor = cursorColor;
            return this;
        }

        public EditBoxWidget.Builder hasBackground(boolean hasBackground) {
            this.hasBackground = hasBackground;
            return this;
        }

        public EditBoxWidget.Builder hasOverlay(boolean hasOverlay) {
            this.hasOverlay = hasOverlay;
            return this;
        }

        public EditBoxWidget build(TextRenderer textRenderer, int width, int height, Text message) {
            return new EditBoxWidget(
                    textRenderer,
                    this.x,
                    this.y,
                    width,
                    height,
                    this.placeholder,
                    message,
                    this.textColor,
                    this.textShadow,
                    this.cursorColor,
                    this.hasBackground,
                    this.hasOverlay
            );
        }
    }
}
