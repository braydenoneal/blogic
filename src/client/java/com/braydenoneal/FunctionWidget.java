package com.braydenoneal;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FunctionWidget extends ClickableWidget implements LayoutWidget {
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/button");
    private static final List<Integer> COLORS = List.of(
            0x00000000,
            0x3FFF0000,
            0x3FFFFF00,
            0x3F00FF00,
            0x3F00FFFF,
            0x3F0000FF,
            0x3FFF00FF
    );

    private final GridWidget grid;
    private int currentIndex = 0;
    private final List<ClickableWidget> widgets = new ArrayList<>();
    private final ControllerScreen screen;
    private final int level;

    public FunctionWidget(int x, int y, ControllerScreen screen, Function function, int level) {
        super(x, y, 0, 0, Text.of(""));
        this.grid = new GridWidget(x, y);
        this.screen = screen;
        this.level = level;

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

        refreshPositions();
    }

    public void add(Widget widget) {
        if (widget instanceof ClickableWidget clickableWidget) {
            widgets.add(clickableWidget);
        }

        grid.add(widget, 0, currentIndex++, positioner -> positioner.margin(4).alignVerticalCenter());
    }

    public void addParameter(Either<Terminal, Function> parameter) {
        if (parameter.left().isPresent()) {
            add(new TerminalWidget(0, 0, screen, parameter.left().get()));
        } else if (parameter.right().isPresent()) {
            add(new FunctionWidget(0, 0, screen, parameter.right().get(), (level + 1) % COLORS.toArray().length));
        }
    }

    public void addLabel(String text) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        add(new TextWidget(textRenderer.getWidth(text), 20, Text.of(text), textRenderer));
    }

    public void addTextField(String value) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        TextFieldWidget textFieldWidget = new TextFieldWidget(textRenderer, textRenderer.getWidth(value) + 10, 20, Text.of(""));
        textFieldWidget.setText(value);
        add(textFieldWidget);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(), getWidth(), getHeight());
        context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), COLORS.get(level));
    }

    public boolean isMouseOverChild(double mouseX, double mouseY) {
        for (ClickableWidget widget : widgets) {
            if (widget instanceof FunctionWidget functionWidget && functionWidget.isMouseOverChild(mouseX, mouseY)) {
                return true;
            } else if (widget.isMouseOver(mouseX, mouseY)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (isMouseOverChild(mouseX, mouseY)) {
            return false;
        }

        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {
        consumer.accept(this);
        forEachElement(element -> element.forEachChild(consumer));
    }

    @Override
    public void forEachElement(Consumer<Widget> consumer) {
        grid.forEachElement(consumer);
    }

    @Override
    public void refreshPositions() {
        this.grid.refreshPositions();
    }

    @Override
    public int getWidth() {
        return this.grid.getWidth();
    }

    @Override
    public int getHeight() {
        return this.grid.getHeight();
    }

    @Override
    public void setX(int x) {
        this.grid.setX(x);
    }

    @Override
    public void setY(int y) {
        this.grid.setY(y);
    }

    @Override
    public int getX() {
        return this.grid.getX();
    }

    @Override
    public int getY() {
        return this.grid.getY();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
