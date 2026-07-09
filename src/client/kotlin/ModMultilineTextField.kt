import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.MultilineTextField
import net.minecraft.client.gui.components.Whence
import net.minecraft.util.Mth
import java.util.*

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
        val left = Mth.floor(x)
        Objects.requireNonNull(font)
        val top = Mth.floor(y / lineHeight)
        val lineView = displayLines[Mth.clamp(top, 0, displayLines.size - 1)]
        val clickedColumn = font.plainSubstrByWidth(value.substring(lineView.beginIndex, lineView.endIndex), left).length
        seekCursor(Whence.ABSOLUTE, lineView.beginIndex + clickedColumn)
    }
}
