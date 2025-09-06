package com.braydenoneal.blang.parser.expression.builtin.list

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class ListRemoveBuiltin(
    val name: String,
    val listValue: ListValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val removeValue = arguments.anyValue(program, "value", 0)
        val localList: MutableList<Value<*>> = listValue.value

        if (removeValue is IntegerValue) {
            localList.removeAt(removeValue.value)
        } else {
            localList.remove(removeValue)
        }

        return program.scope.set(name, ListValue(localList))
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_REMOVE_BUILTIN

    companion object {
        val CODEC: MapCodec<ListRemoveBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("name").forGetter(ListRemoveBuiltin::name),
                ListValue.CODEC.fieldOf("listValue").forGetter(ListRemoveBuiltin::listValue),
                Arguments.CODEC.fieldOf("arguments").forGetter(ListRemoveBuiltin::arguments)
            ).apply(instance, ::ListRemoveBuiltin)
        }
    }
}
