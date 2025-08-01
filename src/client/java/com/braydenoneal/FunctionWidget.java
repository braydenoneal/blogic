package com.braydenoneal;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Map;

public class FunctionWidget extends DirectionalLayoutWidget implements Drawable, Element, Selectable {
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/button");

    public FunctionWidget(int x, int y, ControllerScreen screen, Function function) {
        super(x, y, DisplayAxis.HORIZONTAL);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        add(new TextWidget(Text.of(function.getName()), textRenderer), positioner -> positioner.margin(5).alignVerticalCenter());

        for (Map.Entry<String, Either<Terminal, Function>> parameter : function.getParameters().entrySet()) {
            add(new TextWidget(Text.of(parameter.getKey()), textRenderer), positioner -> positioner.margin(5).alignVerticalCenter());

            if (parameter.getValue().left().isPresent()) {
                add(new TerminalWidget(0, 0, screen, parameter.getValue().left().get()), positioner -> positioner.margin(5).alignVerticalCenter());
            } else if (parameter.getValue().right().isPresent()) {
                add(new FunctionWidget(0, 0, screen, parameter.getValue().right().get()), positioner -> positioner.margin(5).alignVerticalCenter());
            }
        }

        screen.addDrawableChild(this);
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
