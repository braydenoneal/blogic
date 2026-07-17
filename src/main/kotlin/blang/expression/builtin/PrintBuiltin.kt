package blang.expression.builtin

import blang.BlogicProgram
import net.minecraft.network.chat.Component
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.Null
import program.expression.value.StringValue
import program.expression.value.Value

data class PrintBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val value = if (arguments.namelessArguments.isEmpty()) StringValue("") else (arguments.anyValue(program, "value", 0) ?: return null)
        var string = value.toString()

        if (value is StringValue) {
            string = string.substring(1, string.length - 1)
        }

        val world = program.context.entity.level

        if (world != null && world.server != null) {
            for (player in world.server?.playerList?.players!!) {
                player.sendSystemMessage(Component.nullToEmpty(string))
            }
        }

        return Null.VALUE
    }
}
