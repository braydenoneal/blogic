import block.entity.ControllerScreenHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.input.KeyEvent
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import networking.StringPayload

class ControllerScreen(handler: ControllerScreenHandler, inventory: Inventory, title: Component) : AbstractContainerScreen<ControllerScreenHandler>(handler, inventory, title) {
    override fun init() {
        super.init()
        val editBoxWidget = EditBoxWidget.builder().x(20).y(20).build(Minecraft.getInstance().font, width - 40, height - 80, Component.nullToEmpty(""))
        editBoxWidget.text = menu.source()
        addRenderableWidget<EditBoxWidget>(editBoxWidget)
        addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE) {
                menu.setSource(editBoxWidget.text)
                ClientPlayNetworking.send(StringPayload(menu.pos(), menu.source()))
                onClose()
            }.bounds(width / 2 - 4 - 150, height - 40, 150, 20).build(),
        )
        addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL) { onClose() }.bounds(width / 2 + 4, height - 40, 150, 20).build())
        focused = editBoxWidget
    }

    public override fun <T> addRenderableWidget(drawableElement: T): T where T : GuiEventListener, T : Renderable, T : NarratableEntry {
        return super.addRenderableWidget(drawableElement)
    }

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
