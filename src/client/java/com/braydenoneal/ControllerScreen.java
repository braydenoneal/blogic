package com.braydenoneal;

import com.braydenoneal.block.entity.ControllerScreenHandler;
import com.braydenoneal.networking.StringPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ControllerScreen extends HandledScreen<ControllerScreenHandler> {
    public ControllerScreen(ControllerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        EditBoxWidget editBoxWidget = new EditBoxWidget.Builder().x(20).y(20).build(MinecraftClient.getInstance().textRenderer, width - 40, height - 80, Text.of(""));
        editBoxWidget.setText(handler.source());
        addDrawableChild(editBoxWidget);
        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            handler.setSource(editBoxWidget.getText());
            ClientPlayNetworking.send(new StringPayload(handler.pos(), handler.source()));
            close();
        }).dimensions(width / 2 - 4 - 150, height - 40, 150, 20).build());
        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> close()).dimensions(width / 2 + 4, height - 40, 150, 20).build());
        setFocused(editBoxWidget);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
    }

    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (client != null && client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
