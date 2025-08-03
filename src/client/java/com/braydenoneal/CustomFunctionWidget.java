package com.braydenoneal;

import com.braydenoneal.data.controller.function.CustomFunction;
import com.braydenoneal.data.controller.function.Function;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;

public class CustomFunctionWidget extends DirectionalLayoutWidget implements Drawable, Element, Selectable {
    public CustomFunctionWidget(int x, int y, ControllerScreen screen, CustomFunction customFunction) {
        super(x, y, DisplayAxis.VERTICAL);
        screen.addDrawableChild(this);

        add(new CustomFunctionHeaderWidget(0, 0, screen, customFunction));

        for (Function function : customFunction.body()) {
            add(new FunctionWidget(0, 0, screen, function, 0));
        }

        refreshPositions();
        forEachChild(screen::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
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
