package com.braydenoneal.blang.parser.expression.builtin.list

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class ListPopBuiltin(
    val name: String,
    val listValue: ListValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val localList: MutableList<Value<*>> = listValue.value()
        localList.removeLast()

        return program.scope.set(name, ListValue(localList))
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_POP_BUILTIN

    companion object {
        val CODEC: MapCodec<ListPopBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("name").forGetter(ListPopBuiltin::name),
                ListValue.CODEC.fieldOf("listValue").forGetter(ListPopBuiltin::listValue),
                Arguments.CODEC.fieldOf("arguments").forGetter(ListPopBuiltin::arguments)
            ).apply(instance, ::ListPopBuiltin)
        }
    }
}
