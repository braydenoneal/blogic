package controller

import net.minecraft.client.gui.Font
import net.minecraft.client.input.CharacterEvent

class ConsoleEditBox(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : MonospaceEditBox(x, y, width, height) {
    init {
        textField = ConsoleTextField(font, width - totalInnerPadding(), lineHeight)
        textField.setCursorListener { scrollToCursor() }
    }

    override fun charTyped(event: CharacterEvent): Boolean {
        return false
    }

    class ConsoleTextField(
        font: Font,
        width: Int,
        lineHeight: Int,
    ) : MonospaceTextField(font, width, lineHeight) {
        override fun insertText(input: String) {
        }

        override fun deleteText(dir: Int) {
        }
    }
}
