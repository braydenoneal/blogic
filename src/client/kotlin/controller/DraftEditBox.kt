package controller

import block.entity.ControllerBlockEntity
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import parser.tokenizer.Type
import program.expression.builtin.BuiltinFunctions
import java.util.regex.Pattern
import kotlin.math.log10
import kotlin.math.max

class DraftEditBox(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val entity: ControllerBlockEntity,
) : MonospaceEditBox(x, y, width, height) {
    override val leftPad: Int get() = font.width(monospaceText(" ".repeat(lineDigits + 2)))
    private val lineDigits: Int get() = (log10(textField.displayLines.size.toDouble()) + 1).toInt()
    var matches: List<String> = listOf()
    var match: String = ""

    context(graphics: GuiGraphicsExtractor, text: String)
    fun drawSuggestions(lineNumber: Int, begin: Int, end: Int) {
        if (matches.isNotEmpty() && textField.cursor >= begin && textField.cursor <= end) {
            val maxLength = matches.maxOf { it.length }
            val width = font.width(monospaceText(" ".repeat(maxLength)))
            val height = lineHeight * matches.size
            val lineUntilPos = text.substring(begin, textField.cursor - match.length)
            val x = innerLeft + font.width(monospaceText(lineUntilPos)) + leftPad
            val textY = textY(lineNumber)
            graphics.fill(x, textY + lineHeight, x + width, textY + lineHeight + height, Colors.SUGGESTIONS_BACKGROUND)
            var y = textY + lineHeight

            for (string in matches) {
                graphics.text(font, monospaceText(string), x, y, Colors.TEXT)
                y += lineHeight
            }
        }
    }

    context(graphics: GuiGraphicsExtractor, text: String)
    override fun drawLine(lineNumber: Int, begin: Int, end: Int) {
        var x = innerLeft
        val y = textY(lineNumber)
        var position = 0

        graphics.text(
            font,
            monospaceText(lineNumber.toString().padStart(lineDigits, ' ') + " "),
            x,
            y,
            Colors.LINE_NUMBER,
        )

        x += leftPad

        while (position < text.length) {
            var error = true

            for (type in Type.entries) {
                val matcher = type.regex.matcher(text.substring(position))

                if (matcher.find()) {
                    val group = matcher.group()

                    var color = when (type) {
                        Type.COMMENT -> Colors.COMMENT

                        Type.INTEGER,
                        Type.FLOAT,
                            -> Colors.NUMBER

                        Type.QUOTE,
                        Type.QUOTE_START,
                        Type.QUOTE_MIDDLE,
                        Type.QUOTE_END,
                            -> Colors.STRING

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
                            -> Colors.KEYWORD

                        else -> Colors.TEXT
                    }

                    if (type == Type.IDENTIFIER && position + group.length < text.length && text[position + group.length] == '(') {
                        color = Colors.FIELD
                    }

                    graphics.text(font, group, x, y, color)
                    position += group.length
                    x += font.width(monospaceText(group))
                    error = false
                    break
                }
            }

            if (error) {
                graphics.text(font, text.substring(position), x, y, Colors.ERROR)
                break
            }
        }

        drawSuggestions(lineNumber, begin, end)
    }

    context(graphics: GuiGraphicsExtractor)
    override fun drawBackground() {
        super.drawBackground()
        graphics.fill(
            innerLeft + leftPad - font.width(monospaceText(" ")),
            y,
            innerLeft + leftPad - font.width(monospaceText(" ")) + 1,
            y + max(getHeight(), textField.lineCount * lineHeight + 6),
            Colors.GUTTER_GUIDE,
        )
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
}
