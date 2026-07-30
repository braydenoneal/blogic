package blang.expression.builtin

import blang.BlogicProgram
import net.minecraft.network.chat.Component
import program.Program
import program.expression.Arguments
import program.expression.value.StringValue
import program.expression.value.Value
import program.expression.value.util.Null

object PrintBuiltin {
    fun call(program: Program, arguments: Arguments): Value<*> {
        val program = BlogicProgram.cast(program)
        val value = arguments.getAny(program, "value", StringValue(""))
        var string = if (value is StringValue) value.value else value.toString()

        val world = program.context.entity.level

        if (world != null && world.server != null) {
            for (player in world.server?.playerList?.players!!) {
                player.sendSystemMessage(Component.nullToEmpty(string))
            }
        }

        return Null.VALUE
    }
}
