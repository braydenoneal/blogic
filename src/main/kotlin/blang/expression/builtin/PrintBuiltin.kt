package blang.expression.builtin

import blang.BlogicProgram
import net.minecraft.network.chat.Component
import program.Program
import program.expression.Arguments
import program.expression.builtin.Builtin
import program.expression.value.Null
import program.expression.value.StringValue
import program.expression.value.Value

data class PrintBuiltin(override val arguments: Arguments) : Builtin(arguments) {
    override fun innerEvaluate(program: Program): Value<*> {
        val program = BlogicProgram.cast(program)
        val value = arguments.getAny(program, "value", StringValue(""))
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
