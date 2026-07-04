package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BlockValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.math.BlockPos


data class GetBlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val x = arguments.integerValue(program, "x", 0)
        val y = arguments.integerValue(program, "y", 1)
        val z = arguments.integerValue(program, "z", 2)

        val entityPos = program.context().pos
        val pos = BlockPos(entityPos.x + x.value, entityPos.y + y.value, entityPos.z + z.value)
        val world = program.context().entity!!.getWorld()

        if (world == null) {
            throw RunException("World is null")
        }

        return BlockValue(world.getBlockState(pos).block)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.GET_BLOCK_BUILTIN

    companion object {
        val CODEC: MapCodec<GetBlockBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(GetBlockBuiltin::arguments)
            ).apply(it, ::GetBlockBuiltin)
        }
    }
}
