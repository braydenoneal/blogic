package controller

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.MultiLineEditBox
import net.minecraft.client.input.CharacterEvent
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.resources.Identifier
import net.minecraft.util.Util
import kotlin.math.max

class ConsoleBox(
    font: Font,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    placeholder: Component,
    narration: Component,
    textColor: Int,
    textShadow: Boolean,
    cursorColor: Int,
    showBackground: Boolean,
    showDecorations: Boolean,
) : MultiLineEditBox(font, x, y, width, height, placeholder, narration, textColor, textShadow, cursorColor, showBackground, showDecorations) {
    companion object {
        const val LINE_HEIGHT: Int = 11
        const val CURSOR_BLINK_INTERVAL: Int = 500
        val MONOSPACE_FONT: FontDescription = FontDescription.Resource(Identifier.fromNamespaceAndPath("blogic", "monospace"))

        fun builder(): Builder = Builder()

        fun monospaceText(string: String): Component {
            val component = Component.literal(string)
            component.style = component.style.withFont(MONOSPACE_FONT)
            return component
        }
    }

    init {
        textField = ConsoleTextField(font, width - totalInnerPadding(), LINE_HEIGHT)
        textField.setCursorListener { scrollToCursor() }
    }

    override fun charTyped(event: CharacterEvent): Boolean {
        return false
    }

    private fun drawMonospace(context: GuiGraphicsExtractor, string: String, x: Int, y: Int) {
        val component = Component.literal(string)
        component.style = component.style.withFont(MONOSPACE_FONT)
        context.text(font, component, x, y, -0x43413c, false)
    }

    override fun seekCursorScreen(x: Double, y: Double) {
        val mouseX = x - this.x.toDouble() - innerPadding().toDouble()
        val mouseY = y - this.y.toDouble() - innerPadding().toDouble() + scrollAmount()
        textField.seekCursorToPoint(mouseX, mouseY)
    }

    override fun getInnerHeight(): Int {
        return LINE_HEIGHT * textField.lineCount
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        graphics.fill(x, y, x + getWidth(), y + max(getHeight(), textField.lineCount * LINE_HEIGHT + 6), -0xe1e0de)
        val text: String = textField.value

        val pos = textField.cursor
        val focused = isFocused && (Util.getMillis() - focusedTime) / CURSOR_BLINK_INTERVAL % 2L == 0L
        var x: Int
        var textY = innerTop + 2
        var lineNumber = 1

        for (line in textField.displayLines) {
            val isVisible = withinContentAreaTopBottom(textY, textY + LINE_HEIGHT)
            val textX = innerLeft

            if (isVisible) {
                val fullLineText = text.substring(line.beginIndex, line.endIndex)
                drawMonospace(graphics, fullLineText, textX, textY)
            }

            if (focused && pos >= line.beginIndex && pos <= line.endIndex) {
                if (isVisible) {
                    val lineUntilPos = text.substring(line.beginIndex, pos)
                    x = textX + font.width(monospaceText(lineUntilPos))
                    graphics.fill(x, textY - 2, x + 1, textY + 9, -0x1f000001)
                }
            }

            textY += LINE_HEIGHT
            lineNumber++
        }

        if (textField.hasSelection()) {
            val substring2 = textField.selected
            val n = innerLeft
            textY = innerTop

            for (substring3 in textField.displayLines) {
                if (substring2.beginIndex <= substring3.endIndex) {
                    if (substring3.beginIndex > substring2.endIndex) {
                        break
                    }

                    if (withinContentAreaTopBottom(textY, textY + LINE_HEIGHT)) {
                        val o: Int = font.width(
                            monospaceText(
                                text.substring(
                                    substring3.beginIndex,
                                    max(substring2.beginIndex, substring3.beginIndex),
                                ),
                            ),
                        )

                        val p: Int = if (substring2.endIndex > substring3.endIndex) {
                            width - innerPadding()
                        } else {
                            font.width(monospaceText(text.substring(substring3.beginIndex, substring2.endIndex)))
                        }

                        graphics.textHighlight(
                            n + o,
                            textY,
                            n + p,
                            textY + LINE_HEIGHT,
                            true,
                        )
                    }
                }

                textY += LINE_HEIGHT
            }
        }
    }

    override fun scrollToCursor() {
        var scrollAmount = scrollAmount()
        val firstFullyVisibleLine = textField.getLineView((scrollAmount / LINE_HEIGHT).toInt())

        if (textField.cursor() <= firstFullyVisibleLine.beginIndex()) {
            scrollAmount = (textField.lineAtCursor * LINE_HEIGHT).toDouble()
        } else {
            val lastFullyVisibleLine = textField.getLineView(((scrollAmount + height.toDouble()) / LINE_HEIGHT).toInt() - 1)

            if (textField.cursor() > lastFullyVisibleLine.endIndex()) {
                scrollAmount = (textField.lineAtCursor * LINE_HEIGHT - height + LINE_HEIGHT + totalInnerPadding()).toDouble()
            }
        }

        setScrollAmount(scrollAmount)
    }

    class Builder {
        private var x = 0
        private var y = 0
        private var placeholder: Component = CommonComponents.EMPTY
        private var textColor = -2039584
        private var textShadow = true
        private var cursorColor = -3092272
        private var showBackground = true
        private var showDecorations = true

        fun setX(x: Int): Builder {
            this.x = x
            return this
        }

        fun setY(y: Int): Builder {
            this.y = y
            return this
        }

        fun build(font: Font, width: Int, height: Int, narration: Component): ConsoleBox {
            return ConsoleBox(
                font,
                x,
                y,
                width,
                height,
                placeholder,
                narration,
                textColor,
                textShadow,
                cursorColor,
                showBackground,
                showDecorations,
            )
        }
    }
}
