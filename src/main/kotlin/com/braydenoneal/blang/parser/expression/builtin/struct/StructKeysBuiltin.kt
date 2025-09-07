package com.braydenoneal.blang.parser.expression.builtin.struct

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.StringValue
import com.braydenoneal.blang.parser.expression.value.StructValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class StructKeysBuiltin(
    val struct: StructValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val list: MutableList<Value<*>> = ArrayList()

        for (entry in struct.value) {
            list.add(StringValue(entry.first))
        }

        return ListValue(list)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.STRUCT_KEYS_BUILTIN

    companion object {
        val CODEC: MapCodec<StructKeysBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                StructValue.CODEC.fieldOf("struct").forGetter(StructKeysBuiltin::struct),
                Arguments.CODEC.fieldOf("arguments").forGetter(StructKeysBuiltin::arguments)
            ).apply(instance, ::StructKeysBuiltin)
        }
    }
}
