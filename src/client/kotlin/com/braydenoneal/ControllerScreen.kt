package com.braydenoneal

import com.braydenoneal.block.entity.ControllerScreenHandler
import com.braydenoneal.networking.StringPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class ControllerScreen(handler: ControllerScreenHandler, inventory: PlayerInventory, title: Text) : HandledScreen<ControllerScreenHandler>(handler, inventory, title) {
    override fun init() {
        super.init()
        val editBoxWidget = EditBoxWidget.builder().x(20).y(20).build(MinecraftClient.getInstance().textRenderer, width - 40, height - 80, Text.of(""))
        editBoxWidget.text = handler.source()
        addDrawableChild<EditBoxWidget>(editBoxWidget)
        addDrawableChild<ButtonWidget>(ButtonWidget.builder(ScreenTexts.DONE) {
            handler.setSource(editBoxWidget.text)
            ClientPlayNetworking.send(StringPayload(handler.pos(), handler.source()))
            close()
        }.dimensions(width / 2 - 4 - 150, height - 40, 150, 20).build())
        addDrawableChild<ButtonWidget>(ButtonWidget.builder(ScreenTexts.CANCEL) { close() }.dimensions(width / 2 + 4, height - 40, 150, 20).build())
        focused = editBoxWidget
    }

    override fun drawForeground(context: DrawContext, mouseX: Int, mouseY: Int) {
    }

    override fun drawBackground(context: DrawContext, deltaTicks: Float, mouseX: Int, mouseY: Int) {
    }

    public override fun <T> addDrawableChild(drawableElement: T): T where T : Element, T : Drawable, T : Selectable {
        return super.addDrawableChild<T>(drawableElement)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (client != null && client!!.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            return true
        }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        children().first().mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }
}
