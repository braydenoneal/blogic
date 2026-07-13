import block.entity.ControllerScreenHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.input.KeyEvent
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import networking.ControllerPayload

class ControllerScreen(handler: ControllerScreenHandler, inventory: Inventory, title: Component) : AbstractContainerScreen<ControllerScreenHandler>(handler, inventory, title) {
    var nameEditBox: EditBox? = null
    var multiLineEditBox: ModMultiLineEditBox? = null

    override fun init() {
        super.init()

        nameEditBox = EditBox(font, width - 40, 20, Component.nullToEmpty("name"))
        nameEditBox!!.x = 20
        nameEditBox!!.y = 20
        nameEditBox!!.value = menu.name()
        addRenderableWidget(nameEditBox!!)

        multiLineEditBox = ModMultiLineEditBox.builder().setX(20).setY(60).build(
            font,
            width - 40,
            height - 120,
            Component.nullToEmpty("source"),
        )

        multiLineEditBox!!.value = menu.draft()
        multiLineEditBox!!.textField.cursor = menu.cursorPosition()
        multiLineEditBox!!.textField.selectCursor = menu.cursorPosition()
        addRenderableWidget(multiLineEditBox!!)

        val buttonWidth = (width - 80) / 3

        addRenderableWidget(
            Button.builder(Component.literal("Save")) {
                val payload = ControllerPayload(menu.pos(), nameEditBox!!.value, multiLineEditBox!!.value, multiLineEditBox!!.textField.cursor, false)
                menu.setSource(payload)
                ClientPlayNetworking.send(payload)
                super.onClose()
            }.bounds(20, height - 40, buttonWidth, 20).build(),
        )

        addRenderableWidget(
            Button.builder(Component.literal("Discard")) {
                val payload = ControllerPayload(menu.pos(), menu.name(), menu.source(), 0, true)
                menu.setSource(payload)
                ClientPlayNetworking.send(payload)
                super.onClose()
            }.bounds(40 + buttonWidth, height - 40, buttonWidth, 20).build(),
        )

        addRenderableWidget(
            Button.builder(Component.literal("Close")) { onClose() }.bounds(60 + buttonWidth * 2, height - 40, buttonWidth, 20).build(),
        )

        focused = multiLineEditBox
    }

    override fun extractLabels(graphics: GuiGraphicsExtractor, xm: Int, ym: Int) {}

    override fun keyPressed(keyEvent: KeyEvent): Boolean {
        if (minecraft.options.keyInventory.matches(keyEvent)) {
            return true
        }

        return super.keyPressed(keyEvent)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        return children()[1].mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun onClose() {
        val payload = ControllerPayload(menu.pos(), nameEditBox!!.value, multiLineEditBox!!.value, multiLineEditBox!!.textField.cursor, true)
        menu.setSource(payload)
        ClientPlayNetworking.send(payload)
        super.onClose()
    }
}
