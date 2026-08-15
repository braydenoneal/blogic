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
    lateinit var name: EditBox
    lateinit var draft: ModMultiLineEditBox
    lateinit var console: ConsoleBox
    lateinit var discardButton: Button
    lateinit var saveButton: Button
    lateinit var source: String

    override fun init() {
        super.init()

        val pad = 10
        val gap = 10
        val buttonHeight = 20
        val consoleHeight = 80
        val draftHeight = height - pad - buttonHeight - gap - gap - consoleHeight - gap - buttonHeight - gap
        val fullWidth = width - pad * 2
        var y = pad

        source = menu.payload.source

        name = EditBox(font, fullWidth, buttonHeight, Component.nullToEmpty("name"))
        name.x = pad
        name.y = y
        name.value = menu.payload.name
        addRenderableWidget(name)
        y += 20
        y += gap

        draft = ModMultiLineEditBox.builder(menu.entity).setX(pad).setY(y).build(
            font,
            fullWidth,
            draftHeight,
            Component.nullToEmpty("source"),
        )
        y += draftHeight
        y += gap

        draft.value = menu.payload.draft
        draft.textField.cursor = menu.payload.cursor
        draft.textField.selectCursor = menu.payload.cursor
        addRenderableWidget(draft)

        console = ConsoleBox.builder().setX(pad).setY(y).build(
            font,
            fullWidth,
            consoleHeight,
            Component.nullToEmpty("source"),
        )
        console.value = menu.payload.console
        console.textField.cursor = console.textField.value.length - 1
        console.textField.selectCursor = console.textField.value.length - 1
        console.setScrollAmount(console.maxScrollAmount().toDouble())
        addRenderableWidget(console)
        y += consoleHeight
        y += gap

        val buttonWidth = (fullWidth - 3 * gap) / 4
        var x = pad

        addRenderableWidget(
            Button.builder(Component.literal("Save and Run")) {
                sendPayload(run = true)
            }.bounds(x, y, buttonWidth, buttonHeight).build(),
        )
        x += gap + buttonWidth

        saveButton = Button.builder(Component.literal("Save")) {
            sendPayload()
        }.bounds(x, y, buttonWidth, buttonHeight).build()
        x += gap + buttonWidth

        saveButton.active = isSourceChanged()
        addRenderableWidget(saveButton)

        discardButton = Button.builder(Component.literal("Discard")) {
            draft.value = source
            draft.textField.cursor = menu.payload.cursor
            draft.textField.selectCursor = menu.payload.cursor
        }.bounds(x, y, buttonWidth, buttonHeight).build()
        x += gap + buttonWidth

        discardButton.active = isSourceChanged()
        addRenderableWidget(discardButton)

        addRenderableWidget(
            Button.builder(Component.literal("Close")) {
                onClose()
            }.bounds(x, y, buttonWidth, buttonHeight).build(),
        )

        focused = draft
    }

    fun sendPayload(isDraft: Boolean = false, run: Boolean = false) {
        val payload = ControllerPayload(
            menu.payload.pos,
            name.value,
            source,
            draft.value,
            console.value,
            draft.textField.cursor,
            isDraft,
            run,
        )

        if (!isDraft) {
            source = draft.value
        }

        menu.setSource(payload)
        ClientPlayNetworking.send(payload)
    }

    private fun isSourceChanged(): Boolean = draft.value != source

    fun updateConsole(value: String) {
        console.value = value
        console.textField.cursor = console.textField.value.length - 1
        console.textField.selectCursor = console.textField.value.length - 1
        console.setScrollAmount(console.maxScrollAmount().toDouble())
    }

    override fun containerTick() {
        super.containerTick()
        saveButton.active = isSourceChanged()
        discardButton.active = isSourceChanged()
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
        return focused?.mouseScrolled(
            mouseX,
            mouseY,
            horizontalAmount,
            verticalAmount
        ) ?: false
    }

    override fun onClose() {
        sendPayload(isDraft = true)
        super.onClose()
    }
}
