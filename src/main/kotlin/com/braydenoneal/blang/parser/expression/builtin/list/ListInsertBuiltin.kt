package com.braydenoneal.blang.parser.expression.builtin.list

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class ListInsertBuiltin(
    val listValue: ListValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val index = arguments.integerValue(program, "index", 0).value
        val insertValue = arguments.anyValue(program, "value", 1)
        listValue.value.add(index, insertValue)
        return listValue
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_INSERT_BUILTIN

    companion object {
        val CODEC: MapCodec<ListInsertBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ListValue.CODEC.fieldOf("listValue").forGetter(ListInsertBuiltin::listValue),
                Arguments.CODEC.fieldOf("arguments").forGetter(ListInsertBuiltin::arguments)
            ).apply(instance, ::ListInsertBuiltin)
        }
    }
}
