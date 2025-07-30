package com.braydenoneal;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TerminalWidget extends ClickableWidget {
    private final TextFieldWidget textFieldWidget;
    private final TextRenderer textRenderer;

    public TerminalWidget(int x, int y, int width, int height, Text message, TextRenderer textRenderer) {
        super(x, y, width, height, message);
        this.textFieldWidget = new TextFieldWidget(textRenderer, x + 100, y, 250, 20, Text.of("1"));
        this.textRenderer = textRenderer;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF6B6B6B);
        drawScrollableText(context, textRenderer, 4, 0xFFFFFFFF);
        textFieldWidget.renderWidget(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        textFieldWidget.onClick(mouseX, mouseY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        textFieldWidget.onRelease(mouseX, mouseY);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (this.textFieldWidget.mouseClicked(mouseX, mouseY, button)) {
//            return true;
//        }
//        return super.mouseClicked(mouseX, mouseY, button);
//    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return textFieldWidget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isFocused() {
        return textFieldWidget.isFocused();
    }

    @Override
    public boolean isSelected() {
        return textFieldWidget.isSelected();
    }
}
