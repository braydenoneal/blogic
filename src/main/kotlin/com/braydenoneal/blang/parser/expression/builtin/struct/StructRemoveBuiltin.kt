package com.braydenoneal.blang.parser.expression.builtin.struct

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.StringValue
import com.braydenoneal.blang.parser.expression.value.StructValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class StructRemoveBuiltin(
    val struct: StructValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val removeValue = arguments.anyValue(program, "value", 0)

        if (removeValue is StringValue) {
            for (i in struct.value.indices) {
                if (struct.value[i].first == removeValue.value) {
                    struct.value.removeAt(i)
                }
            }
        }

        return struct
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.STRUCT_REMOVE_BUILTIN

    companion object {
        val CODEC: MapCodec<StructRemoveBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                StructValue.CODEC.fieldOf("struct").forGetter(StructRemoveBuiltin::struct),
                Arguments.CODEC.fieldOf("arguments").forGetter(StructRemoveBuiltin::arguments)
            ).apply(it, ::StructRemoveBuiltin)
        }
    }
}
