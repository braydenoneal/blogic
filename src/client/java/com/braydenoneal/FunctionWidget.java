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
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class FunctionWidget extends DirectionalLayoutWidget implements Drawable, Element, Selectable {
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/button");
    private final ControllerScreen screen;
    private final int level;
    private final List<Integer> colors;

    public FunctionWidget(int x, int y, ControllerScreen screen, Function function, int level) {
        super(x, y, DisplayAxis.HORIZONTAL);
        this.screen = screen;
        this.level = level;
        this.colors = List.of(
                0x2FFF0000,
                0x2FFFFF00,
                0x2F00FF00,
                0x2F00FFFF,
                0x2F0000FF,
                0x2FFF00FF
        );
        screen.addDrawableChild(this);

        add(new EmptyWidget(4, 20));

        for (Function.GuiComponent component : function.getGuiComponents()) {
            if (component instanceof Function.LabelGuiComponent(String text)) {
                addLabel(text);
            } else if (component instanceof Function.TextFieldGuiComponent(String value)) {
                addTextField(value);
            } else if (component instanceof Function.ParameterGuiComponent(Either<Terminal, Function> parameter)) {
                addParameter(parameter);
            }
        }

        add(new EmptyWidget(4, 20));

        forEachChild(screen::addDrawableChild);
        refreshPositions();
    }

    public void addParameter(Either<Terminal, Function> parameter) {
        if (parameter.left().isPresent()) {
            add(new TerminalWidget(0, 0, screen, parameter.left().get()),
                    positioner -> positioner.margin(4).alignVerticalCenter());
        } else if (parameter.right().isPresent()) {
            add(new FunctionWidget(0, 0, screen, parameter.right().get(), (level + 1) % colors.toArray().length),
                    positioner -> positioner.margin(4).alignVerticalCenter());
        }
    }

    public void addLabel(String text) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        add(new TextWidget(textRenderer.getWidth(text), 20, Text.of(text), textRenderer), positioner -> positioner.margin(4).alignVerticalCenter());
    }

    public void addTextField(String value) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        TextFieldWidget valueText = new TextFieldWidget(textRenderer, textRenderer.getWidth(value) + 10, 20, Text.of(""));
        valueText.setText(value);
        add(valueText, positioner -> positioner.margin(4).alignVerticalCenter());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(), getWidth(), getHeight());
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), colors.get(level));
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
