package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.ItemValue
import com.braydenoneal.blang.parser.expression.value.Null
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class BlockItemBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val item = arguments.blockValue(program, "block", 0).value.asItem()

        return if (item == null) {
            Null.VALUE
        } else {
            ItemValue(item)
        }
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.BLOCK_ITEM_BUILTIN

    companion object {
        val CODEC: MapCodec<BlockItemBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(BlockItemBuiltin::arguments)
            ).apply(it, ::BlockItemBuiltin)
        }
    }
}
