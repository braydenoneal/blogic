package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BlockValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier


data class BlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return BlockValue(Registries.BLOCK.get(Identifier.of(arguments.stringValue(program, "value", 0).value())))
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.BLOCK_BUILTIN

    companion object {
        val CODEC: MapCodec<BlockBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(BlockBuiltin::arguments)
            ).apply(instance, ::BlockBuiltin)
        }
    }
}
