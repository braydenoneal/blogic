package blang.expression.builtin

import blang.BlogicProgram
import blang.Context
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerLevel
import networking.ControllerPayload
import program.Program
import program.expression.Arguments
import program.expression.value.Value
import program.expression.value.getAny
import program.expression.value.nullvalue.Null
import program.expression.value.string.StringValue


object PrintBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val value = getAny("value", StringValue(""))
        var string = value.toString()

        if (value is StringValue) {
            string = string.substring(1, string.length - 1)
        }

        val program = BlogicProgram.cast(program.actionProgram)
        program.console += "$string\n"

        val payload = ControllerPayload(
            context.entity.blockPos,
            program.name,
            program.source,
            program.draft,
            program.console,
            program.cursorPosition,
            true
        )

        val level = getLevel()

        for (player in PlayerLookup.level((level as ServerLevel))) {
            ServerPlayNetworking.send(player, payload)
        }

        return Null.VALUE
    }
}
