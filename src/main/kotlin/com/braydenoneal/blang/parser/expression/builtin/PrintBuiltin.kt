package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.Null
import com.braydenoneal.blang.parser.expression.value.StringValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.text.Text


data class PrintBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val value = if (arguments.arguments.isEmpty()) StringValue("") else this.arguments.anyValue(program, "value", 0)
        var string = value.toString()

        if (value is StringValue) {
            string = string.substring(1, string.length - 1)
        }

        if (program.context().entity == null) {
            println(string)
            return Null.VALUE
        }

        val world = program.context().entity!!.getWorld()

        if (world != null && world.server != null) {
            for (player in world.server?.playerManager?.playerList!!) {
                player.sendMessage(Text.of(string))
            }
        }

        return Null.VALUE
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.PRINT_BUILTIN

    companion object {
        val CODEC: MapCodec<PrintBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(PrintBuiltin::arguments)
            ).apply(it, ::PrintBuiltin)
        }
    }
}
