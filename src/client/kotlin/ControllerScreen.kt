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

class ControllerScreen(handler: ControllerScreenHandler, inventory: Inventory, title: Component) :
    AbstractContainerScreen<ControllerScreenHandler>(handler, inventory, title) {
    lateinit var nameEditBox: EditBox
    lateinit var sourceEditBox: ModMultiLineEditBox
    lateinit var console: ConsoleBox
    lateinit var discardButton: Button

    override fun init() {
        super.init()

        var y = 0
        val pad = 20
        y += pad

        nameEditBox = EditBox(font, width - 40, 20, Component.nullToEmpty("name"))
        nameEditBox.x = 20
        nameEditBox.y = y
        nameEditBox.value = menu.payload.name
        addRenderableWidget(nameEditBox)
        y += 20
        y += pad

        sourceEditBox = ModMultiLineEditBox.builder().setX(20).setY(60).build(
            font,
            width - 40,
            height - 20 - 20 - 20 - 20 - 80 - 20 - 20 - 20,
            Component.nullToEmpty("source"),
        )
        y += height - 20 - 20 - 20 - 20 - 80 - 20 - 20 - 20
        y += pad

        sourceEditBox.value = menu.payload.draft
        sourceEditBox.textField.cursor = menu.payload.cursorPosition
        sourceEditBox.textField.selectCursor = menu.payload.cursorPosition
        addRenderableWidget(sourceEditBox)

        console = ConsoleBox.builder().setX(20).setY(y).build(
            font,
            width - 40,
            80,
            Component.nullToEmpty("source"),
        )
        console.value = menu.payload.console
        console.textField.cursor = console.textField.value.length - 1
        console.textField.selectCursor = console.textField.value.length - 1
        console.setScrollAmount(console.maxScrollAmount().toDouble())
        addRenderableWidget(console)

        val buttonWidth = (width - 80) / 3

        addRenderableWidget(
            Button.builder(Component.literal("Save")) {
                val payload = ControllerPayload(
                    menu.payload.pos,
                    nameEditBox.value,
                    sourceEditBox.value,
                    sourceEditBox.value,
                    console.value,
                    sourceEditBox.textField.cursor,
                    false
                )
                menu.setSource(payload)
                ClientPlayNetworking.send(payload)
                super.onClose()
            }.bounds(20, height - 40, buttonWidth, 20).build(),
        )

        discardButton = Button.builder(Component.literal("Discard")) {
            val payload = ControllerPayload(
                menu.payload.pos,
                nameEditBox.value,
                sourceEditBox.value,
                sourceEditBox.value,
                console.value,
                sourceEditBox.textField.cursor,
                true
            )
            menu.setSource(payload)
            ClientPlayNetworking.send(payload)
            super.onClose()
        }.bounds(40 + buttonWidth, height - 40, buttonWidth, 20).build()

        discardButton.active = shouldDiscardActive()
        addRenderableWidget(discardButton)

        addRenderableWidget(
            Button.builder(Component.literal("Close")) { onClose() }
                .bounds(60 + buttonWidth * 2, height - 40, buttonWidth, 20).build(),
        )

        focused = sourceEditBox
    }

    private fun shouldDiscardActive(): Boolean = sourceEditBox.value != menu.payload.source

    fun updateConsole(value: String) {
        console.value = value
        console.textField.cursor = console.textField.value.length - 1
        console.textField.selectCursor = console.textField.value.length - 1
        console.setScrollAmount(console.maxScrollAmount().toDouble())
    }

    override fun containerTick() {
        super.containerTick()
        discardButton.active = shouldDiscardActive()
    }

    override fun extractLabels(graphics: GuiGraphicsExtractor, xm: Int, ym: Int) {}

    override fun keyPressed(keyEvent: KeyEvent): Boolean {
        if (minecraft.options.keyInventory.matches(keyEvent)) {
            return true
        }

        return super.keyPressed(keyEvent)
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        return focused?.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) ?: false
    }

    override fun onClose() {
        val payload =
            ControllerPayload(
                menu.payload.pos,
                nameEditBox.value,
                sourceEditBox.value,
                sourceEditBox.value,
                console.value,
                sourceEditBox.textField.cursor,
                true
            )
        menu.setSource(payload)
        ClientPlayNetworking.send(payload)
        super.onClose()
    }
}
