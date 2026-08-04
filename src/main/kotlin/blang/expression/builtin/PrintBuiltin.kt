package blang.expression.builtin

import blang.BlogicProgram
import net.minecraft.network.chat.Component
import program.Program
import program.expression.Arguments
import program.expression.value.Callable
import program.expression.value.StringValue
import program.expression.value.Value
import program.expression.value.util.Null

object PrintBuiltin : Callable {
    context(program: Program, arguments: Arguments)
    override fun innerCall(): Value<*> {
        val program = BlogicProgram.cast(program.actionProgram)
        val value = arguments.getAny("value", StringValue(""))
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
