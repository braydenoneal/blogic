import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.MultilineTextField
import net.minecraft.client.gui.components.Whence
import net.minecraft.util.Mth
import kotlin.math.max
import kotlin.math.min

class ModMultilineTextField(
    font: Font,
    width: Int,
    val lineHeight: Int,
) : MultilineTextField(font, width) {
    override fun setValue(inValue: String, allowOverflowLineLimit: Boolean) {
        val newValue = truncateFullText(inValue)

        if (allowOverflowLineLimit || !overflowsLineLimit(newValue)) {
            value = newValue
            cursor = 0
            selectCursor = 0
            onValueChange()
        }
    }

    override fun seekCursorToPoint(x: Double, y: Double) {
        val characterWidth = font.width(ModMultiLineEditBox.monospaceText(" "))
        val left = Mth.floor(x / characterWidth)

        val top = Mth.floor(y / lineHeight)
        val lineView = displayLines[Mth.clamp(top, 0, displayLines.size - 1)]

        val clickedColumn = min(max(left, 0), lineView.endIndex - lineView.beginIndex)

        seekCursor(Whence.ABSOLUTE, lineView.beginIndex + clickedColumn)
    }
}
