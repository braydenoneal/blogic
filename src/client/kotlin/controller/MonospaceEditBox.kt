package controller

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.MultiLineEditBox
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.resources.Identifier
import net.minecraft.util.Util
import kotlin.math.max

open class MonospaceEditBox(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : MultiLineEditBox(Minecraft.getInstance().font, x, y, width, height, CommonComponents.EMPTY, CommonComponents.EMPTY, 0, true, 0, true, true) {
    val lineHeight: Int = 11
    val cursorBlinkInterval: Int = 500
    open val leftPad: Int get() = 0

    init {
        textField = MonospaceTextField(font, width - totalInnerPadding(), lineHeight)
        textField.setCursorListener { scrollToCursor() }
    }

    fun textWidth(text: String): Int {
        return font.width(monospaceText(text))
    }

    fun textY(lineNumber: Int): Int {
        return innerTop + lineNumber * lineHeight
    }

    context(graphics: GuiGraphicsExtractor)
    open fun drawBackground() {
        graphics.fill(x, y, x + getWidth(), y + max(getHeight(), (textField.lineCount + 1) * lineHeight), Colors.BACKGROUND)
    }

    context(graphics: GuiGraphicsExtractor, text: String)
    open fun drawLine(lineNumber: Int, begin: Int, end: Int) {
        graphics.text(
            font,
            monospaceText(text.substring(begin, end)),
            innerLeft,
            textY(lineNumber),
            Colors.TEXT,
        )
    }

    context(graphics: GuiGraphicsExtractor, text: String)
    fun drawCursor(lineNumber: Int, begin: Int, end: Int) {
        val showCursor = isFocused && (Util.getMillis() - focusedTime) / cursorBlinkInterval % 2L == 0L

        if (showCursor && textField.cursor >= begin && textField.cursor <= end) {
            val x = innerLeft + textWidth(text.substring(begin, textField.cursor)) + leftPad
            val y = textY(lineNumber)
            graphics.fill(x, y - 2, x + 1, y + 9, Colors.CURSOR)
        }
    }

    context(graphics: GuiGraphicsExtractor, text: String)
    fun drawSelection(lineNumber: Int, begin: Int, end: Int) {
        val selectBegin = textField.selected.beginIndex
        val selectEnd = textField.selected.endIndex

        if (!textField.hasSelection() || selectBegin > end || begin > selectEnd) {
            return
        }

        val left = textWidth(text.substring(begin, max(selectBegin, begin))) + leftPad

        val right: Int = if (selectEnd > end) {
            width - innerPadding()
        } else {
            textWidth(text.substring(begin, selectEnd))
        } + leftPad

        // TODO: Remove comment after testing
        // graphics.textHighlight(innerLeft + left, textY(lineNumber), innerLeft + right, textY(lineNumber + 1), true)
        graphics.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, innerLeft + left, textY(lineNumber), innerLeft + right, textY(lineNumber + 1), Colors.SELECTION_BACKGROUND)
    }

    context(graphics: GuiGraphicsExtractor, text: String)
    fun draw() {
        drawBackground()

        for ((lineNumber, line) in textField.displayLines.withIndex()) {
            if (withinContentAreaTopBottom(textY(lineNumber), textY(lineNumber + 1))) {
                drawLine(lineNumber, line.beginIndex, line.endIndex)
                drawCursor(lineNumber, line.beginIndex, line.endIndex)
                drawSelection(lineNumber, line.beginIndex, line.endIndex)
            }
        }
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        context(graphics, textField.value) { draw() }
    }

    override fun getInnerHeight(): Int {
        return lineHeight * textField.lineCount
    }

    override fun seekCursorScreen(x: Double, y: Double) {
        val mouseX = x - this.x.toDouble() - innerPadding().toDouble() - leftPad
        val mouseY = y - this.y.toDouble() - innerPadding().toDouble() + scrollAmount()
        textField.seekCursorToPoint(mouseX, mouseY)
    }

    override fun scrollToCursor() {
        var scrollAmount = scrollAmount()
        val firstFullyVisibleLine = textField.getLineView((scrollAmount / lineHeight).toInt())

        if (textField.cursor() <= firstFullyVisibleLine.beginIndex()) {
            scrollAmount = (textField.lineAtCursor * lineHeight).toDouble()
        } else {
            val lastFullyVisibleLine = textField.getLineView(((scrollAmount + height.toDouble()) / lineHeight).toInt() - 1)

            if (textField.cursor() > lastFullyVisibleLine.endIndex()) {
                scrollAmount = (textField.lineAtCursor * lineHeight - height + lineHeight + totalInnerPadding()).toDouble()
            }
        }

        setScrollAmount(scrollAmount)
    }

    object Colors {
        const val BACKGROUND = 0x1E1F22
        const val CURRENT_LINE_BACKGROUND = 0x26282E
        const val GUTTER_GUIDE = 0x191A1C
        const val HARD_WRAP_GUIDE = 0x393B40
        const val LINE_NUMBER = 0x4B5059
        const val CURRENT_LINE_NUMBER = 0xA1A3AB
        const val SELECTION_BACKGROUND = 0x214283
        const val SUGGESTIONS_BACKGROUND = 0x2B2D30
        const val SUGGESTIONS_SELECTED = 0x33353B
        const val CURSOR = 0xCED0D6
        const val TEXT = 0xBCBEC4
        const val KEYWORD = 0xCF8E6D
        const val STRING = 0x6AAB73
        const val NUMBER = 0x2AACB8
        const val COMMENT = 0x7A7E85
        const val FIELD = 0xC77DBB
        const val DECLARATION = 0x56A8F5
        const val ANNOTATION = 0xB3AE60
        const val ERROR = 0xF75464
    }

    companion object {
        val monospaceFont: FontDescription = FontDescription.Resource(Identifier.fromNamespaceAndPath("blogic", "monospace"))

        fun monospaceText(string: String): Component {
            val component = Component.literal(string)
            component.style = component.style.withFont(monospaceFont)
            return component
        }
    }
}
