package com.braydenoneal.blang.parser.expression.builtin.list

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class ListRemoveBuiltin(
    val listValue: ListValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val removeValue = arguments.anyValue(program, "value", 0)

        if (removeValue is IntegerValue) {
            listValue.value.removeAt(removeValue.value)
        } else {
            listValue.value.remove(removeValue)
        }

        return listValue
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_REMOVE_BUILTIN

    companion object {
        val CODEC: MapCodec<ListRemoveBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                ListValue.CODEC.fieldOf("listValue").forGetter(ListRemoveBuiltin::listValue),
                Arguments.CODEC.fieldOf("arguments").forGetter(ListRemoveBuiltin::arguments)
            ).apply(it, ::ListRemoveBuiltin)
        }
    }
}
