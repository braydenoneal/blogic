package blang.expression.builtin

import blang.Context
import net.minecraft.network.chat.Component
import program.Program
import program.expression.Arguments
import program.expression.value.StringValue
import program.expression.value.Value
import program.expression.value.getAny
import program.expression.value.util.Null

object PrintBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val value = getAny("value", StringValue(""))
        var string = value.toString()

        if (value is StringValue) {
            string = string.substring(1, string.length - 1)
        }

        val level = getLevel()

        if (level.server != null) {
            for (player in level.server?.playerList?.players!!) {
                player.sendSystemMessage(Component.nullToEmpty(string))
            }
        }

        return Null.VALUE
    }
}
