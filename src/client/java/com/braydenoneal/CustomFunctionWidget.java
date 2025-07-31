package com.braydenoneal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CustomFunctionWidget extends DirectionalLayoutWidget implements Drawable, Element, Selectable {
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/button");

    public CustomFunctionWidget(int x, int y) {
        super(x, y, DisplayAxis.HORIZONTAL);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        add(ButtonWidget.builder(Text.of("Void"), button -> {
        }).size(40, 20).build(), positioner -> positioner.margin(5));
        add(new TextFieldWidget(textRenderer, 100, 20, Text.of("Name")), positioner -> positioner.margin(5));
        add(ButtonWidget.builder(Text.of("Add parameter"), button -> {
        }).size(100, 20).build(), positioner -> positioner.margin(5));
        add(new TerminalWidget(0, 0, Text.of("Int")), positioner -> positioner.margin(5));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void setFocused(boolean focused) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return super.getNavigationFocus();
    }
}
