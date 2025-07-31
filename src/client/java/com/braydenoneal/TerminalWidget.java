package com.braydenoneal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

public class TerminalWidget extends DirectionalLayoutWidget {
    public TerminalWidget(int x, int y, Text label) {
        super(x, y, DisplayAxis.HORIZONTAL);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        add(new TextWidget(label, textRenderer), Positioner::alignVerticalCenter);
        add(new TextFieldWidget(textRenderer, 40, 20, Text.of("Value")), positioner -> positioner.marginX(5));
    }

    @Override
    public void refreshPositions() {
        super.refreshPositions();
        SimplePositioningWidget.setPos(this, getNavigationFocus());
    }
}
