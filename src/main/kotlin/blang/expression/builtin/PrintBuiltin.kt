package blang.expression.builtin

import net.minecraft.text.Text
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Null
import parser.expression.value.StringValue
import parser.expression.value.Value

data class PrintBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is blang.BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val value = if (arguments.arguments.isEmpty()) StringValue("") else (arguments.anyValue(program, "value", 0) ?: return null)
        var string = value.toString()

        if (value is StringValue) {
            string = string.substring(1, string.length - 1)
        }

        val world = program.context.entity.getWorld()

        if (world != null && world.server != null) {
            for (player in world.server?.playerManager?.playerList!!) {
                player.sendMessage(Text.of(string))
            }
        }

        return Null.VALUE
    }
}
