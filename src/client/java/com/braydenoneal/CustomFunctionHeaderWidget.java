package com.braydenoneal;

import com.braydenoneal.data.controller.function.CustomFunction;
import com.braydenoneal.data.controller.parameter.Parameter;
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

import java.util.Map;

public class CustomFunctionHeaderWidget extends DirectionalLayoutWidget implements Drawable, Element, Selectable {
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/button");

    public CustomFunctionHeaderWidget(int x, int y, ControllerScreen screen, CustomFunction customFunction) {
        super(x, y, DisplayAxis.HORIZONTAL);
        screen.addDrawableChild(this);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        add(ButtonWidget.builder(Text.of(customFunction.returnType().getName()), button -> {
        }).size(40, 20).build(), positioner -> positioner.margin(4));

        TextFieldWidget functionNameWidget = new TextFieldWidget(textRenderer, 100, 20, Text.of(""));
        functionNameWidget.setText(customFunction.name());
        add(functionNameWidget, positioner -> positioner.margin(4));

        for (Map.Entry<String, Parameter> entry : customFunction.parameterTypes().entrySet()) {
            add(ButtonWidget.builder(Text.of(entry.getKey()), button -> {
            }).size(40, 20).build(), positioner -> positioner.margin(4));

            TextFieldWidget parameterText = new TextFieldWidget(textRenderer, 100, 20, Text.of(""));
            parameterText.setText(entry.getValue().getName());
            add(parameterText, positioner -> positioner.margin(4));
        }

        add(ButtonWidget.builder(Text.of("+"), button -> {
        }).size(20, 20).build(), positioner -> positioner.margin(4));

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
