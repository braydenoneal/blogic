package com.braydenoneal;

import com.braydenoneal.data.controller.terminal.Terminal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TerminalWidget extends DirectionalLayoutWidget implements Drawable, Element, Selectable {
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/button");

    public TerminalWidget(int x, int y, ControllerScreen screen, Terminal terminal) {
        super(x, y, DisplayAxis.HORIZONTAL);
        screen.addDrawableChild(this);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

//        add(new TextWidget(Text.of(terminal.getName()), textRenderer), positioner -> positioner.margin(4).alignVerticalCenter());

        TextFieldWidget valueText = new TextFieldWidget(textRenderer, 20, 20, Text.of(""));
        valueText.setText(terminal.getValueAsString());
        add(valueText, positioner -> positioner.margin(0).alignVerticalCenter());

        forEachChild(screen::addDrawableChild);
        refreshPositions();
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
    public Selectable.SelectionType getType() {
        return Selectable.SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return super.getNavigationFocus();
    }
}
