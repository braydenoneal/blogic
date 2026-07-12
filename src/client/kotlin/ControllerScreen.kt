import block.entity.ControllerScreenHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.input.KeyEvent
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import networking.ControllerPayload

class ControllerScreen(handler: ControllerScreenHandler, inventory: Inventory, title: Component) : AbstractContainerScreen<ControllerScreenHandler>(handler, inventory, title) {
    override fun init() {
        super.init()

        val nameEditBox = EditBox(font, width - 40, 20, Component.nullToEmpty("name"))
        nameEditBox.x = 20
        nameEditBox.y = 20
        nameEditBox.value = menu.name()
        addRenderableWidget(nameEditBox)

        val multiLineEditBox = ModMultiLineEditBox.builder().setX(20).setY(60).build(
            font,
            width - 40,
            height - 120,
            Component.nullToEmpty("source"),
        )

        multiLineEditBox.value = menu.source()
        addRenderableWidget(multiLineEditBox)

        addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE) {
                val payload = ControllerPayload(menu.pos(), nameEditBox.value, multiLineEditBox.value)
                menu.setSource(payload)
                ClientPlayNetworking.send(payload)
                onClose()
            }.bounds(width / 2 - 4 - 150, height - 40, 150, 20).build(),
        )

        addRenderableWidget(
            Button.builder(CommonComponents.GUI_CANCEL) {
                onClose()
            }.bounds(width / 2 + 4, height - 40, 150, 20).build(),
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
        children().first().mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }
}
