import block.entity.ControllerBlockEntity
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.MultiLineEditBox
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import net.minecraft.util.Util
import parser.tokenizer.Type
import program.expression.builtin.BuiltinFunctions
import java.util.regex.Pattern
import kotlin.math.log10
import kotlin.math.max

class ModMultiLineEditBox(
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
    val entity: ControllerBlockEntity,
) : MultiLineEditBox(
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
    showDecorations
) {
    companion object {
        const val LINE_HEIGHT: Int = 11
        const val CURSOR_BLINK_INTERVAL: Int = 500
        val MONOSPACE_FONT: FontDescription =
            FontDescription.Resource(Identifier.fromNamespaceAndPath("blogic", "monospace"))

        fun builder(entity: ControllerBlockEntity): Builder = Builder(entity)

        fun monospaceText(string: String): Component {
            val component = Component.literal(string)
            component.style = component.style.withFont(MONOSPACE_FONT)
            return component
        }
    }

    init {
        textField = ModMultilineTextField(font, width - totalInnerPadding(), LINE_HEIGHT)
        textField.setCursorListener { scrollToCursor() }
    }

    var matches: List<String> = listOf()
    var match: String = ""

    private val lineDigits get(): Int = (log10(textField.displayLines.size.toDouble()) + 1).toInt()

    private val gutterWidth get(): Int = font.width(monospaceText(" ".repeat(lineDigits + 2)))

    private fun drawMonospace(context: GuiGraphicsExtractor, string: String, x: Int, y: Int, color: Int) {
        val component = Component.literal(string)
        component.style = component.style.withFont(MONOSPACE_FONT)
        context.text(font, component, x, y, color, false)
    }

    private fun drawText(graphics: GuiGraphicsExtractor, text: String, x: Int, y: Int, lineNumber: Int) {
        var x = x
        var position = 0

        drawMonospace(graphics, lineNumber.toString().padStart(lineDigits, ' ') + " ", x, y, ARGB.color(75, 80, 89))
        x += gutterWidth

        while (position < text.length) {
            var error = true

            for (type in Type.entries) {
                val matcher = type.regex.matcher(text.substring(position))

                if (matcher.find()) {
                    val group = matcher.group()

                    var color = when (type) {
                        Type.COMMENT -> -0x85817b

                        Type.INTEGER,
                        Type.FLOAT,
                            -> -0xd55348

                        Type.QUOTE,
                        Type.QUOTE_START,
                        Type.QUOTE_MIDDLE,
                        Type.QUOTE_END,
                            -> -0x95548d

                        Type.IMPORT_KEYWORD,
                        Type.FN_KEYWORD,
                        Type.RETURN_KEYWORD,
                        Type.IF_KEYWORD,
                        Type.ELSE_KEYWORD,
                        Type.ELIF_KEYWORD,
                        Type.WHILE_KEYWORD,
                        Type.FOR_KEYWORD,
                        Type.IN_KEYWORD,
                        Type.BREAK_KEYWORD,
                        Type.CONTINUE_KEYWORD,
                        Type.DEL_KEYWORD,
                        Type.BOOLEAN,
                        Type.AND,
                        Type.OR,
                        Type.NULL,
                            -> -0x307193

                        else -> -0x43413c
                    }

                    if (type == Type.IDENTIFIER && position + group.length < text.length && text[position + group.length] == '(') {
                        color = -0x388245
                    }

                    drawMonospace(graphics, group, x, y, color)
                    position += group.length
                    x += font.width(monospaceText(group))
                    error = false
                    break
                }
            }

            if (error) {
                drawMonospace(graphics, text.substring(position), x, y, -0x8ab9c)
                break
            }
        }
    }

    override fun seekCursorScreen(x: Double, y: Double) {
        val mouseX = x - this.x.toDouble() - innerPadding().toDouble() - gutterWidth
        val mouseY = y - this.y.toDouble() - innerPadding().toDouble() + scrollAmount()
        textField.seekCursorToPoint(mouseX, mouseY)
    }

    override fun getInnerHeight(): Int {
        return LINE_HEIGHT * textField.lineCount
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        graphics.fill(x, y, x + getWidth(), y + max(getHeight(), textField.lineCount * LINE_HEIGHT + 6), -0xe1e0de)
        graphics.fill(
            innerLeft + gutterWidth - font.width(monospaceText(" ")),
            y,
            innerLeft + gutterWidth - font.width(monospaceText(" ")) + 1,
            y + max(getHeight(), textField.lineCount * LINE_HEIGHT + 6),
            ARGB.color(49, 52, 56)
        )
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
                drawText(graphics, fullLineText, textX, textY, lineNumber)
            }

            if (focused && pos >= line.beginIndex && pos <= line.endIndex) {
                if (isVisible) {
                    val lineUntilPos = text.substring(line.beginIndex, pos)
                    x = textX + font.width(monospaceText(lineUntilPos)) + gutterWidth
                    graphics.fill(x, textY - 2, x + 1, textY + 9, -0x1f000001)
                }
            }

            if (matches.isNotEmpty() && pos >= line.beginIndex && pos <= line.endIndex) {
                val maxLength = matches.maxOf { it.length }
                val width = font.width(monospaceText(" ".repeat(maxLength)))
                val height = LINE_HEIGHT * matches.size
                val lineUntilPos = text.substring(line.beginIndex, pos - match.length)
                x = textX + font.width(monospaceText(lineUntilPos)) + gutterWidth
                graphics.fill(x, textY + LINE_HEIGHT, x + width, textY + LINE_HEIGHT + height, ARGB.color(38, 40, 44))
                var y = textY + LINE_HEIGHT

                for (string in matches) {
                    drawMonospace(graphics, string, x, y, -0x43413c)
                    y += LINE_HEIGHT
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
                                    max(substring2.beginIndex, substring3.beginIndex)
                                )
                            )
                        )

                        val p: Int = if (substring2.endIndex > substring3.endIndex) {
                            width - innerPadding()
                        } else {
                            font.width(monospaceText(text.substring(substring3.beginIndex, substring2.endIndex)))
                        }

                        graphics.textHighlight(
                            n + o + gutterWidth,
                            textY,
                            n + p + gutterWidth,
                            textY + LINE_HEIGHT,
                            true
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
            val lastFullyVisibleLine =
                textField.getLineView(((scrollAmount + height.toDouble()) / LINE_HEIGHT).toInt() - 1)

            if (textField.cursor() > lastFullyVisibleLine.endIndex()) {
                scrollAmount =
                    (textField.lineAtCursor * LINE_HEIGHT - height + LINE_HEIGHT + totalInnerPadding()).toDouble()
            }
        }

        setScrollAmount(scrollAmount)
    }

    override fun keyPressed(event: KeyEvent): Boolean {
        val keyPressed = super.keyPressed(event)
        getSuggestions()
        return keyPressed
    }

    override fun charTyped(event: CharacterEvent): Boolean {
        val charTyped = super.charTyped(event)
        getSuggestions()
        return charTyped
    }

    fun getSuggestions() {
        val regex = Pattern.compile("""^([A-Za-z0-9_]*[A-Za-z_])""")
        val string = textField.value.substring(0, textField.cursor).reversed()
        val matcher = regex.matcher(string)

        if (!matcher.find()) {
            matches = listOf()
            return
        }

        val group = matcher.group().reversed()
        match = group

        val variables = entity.program.scope.variables.keys  // TODO: get current scope and all parent scops
        val functions = entity.program.functions.keys
        val imports = entity.program.imports.map { it.name }
        val builtins = BuiltinFunctions.builtins.keys

        val strings = variables + functions + imports + builtins
        matches = strings.filter { it.startsWith(group) }.sorted()
    }

    class Builder(val entity: ControllerBlockEntity) {
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

        fun build(font: Font, width: Int, height: Int, narration: Component): ModMultiLineEditBox {
            return ModMultiLineEditBox(
                font,
                this.x,
                this.y,
                width,
                height,
                this.placeholder,
                narration,
                this.textColor,
                this.textShadow,
                this.cursorColor,
                this.showBackground,
                this.showDecorations,
                this.entity,
            )
        }
    }
}
