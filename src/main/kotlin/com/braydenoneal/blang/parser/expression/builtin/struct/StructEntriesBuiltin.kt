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
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class StructEntriesBuiltin(
    val struct: StructValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val list: MutableList<Value<*>> = ArrayList()

        for (entry in struct.value) {
            list.add(StructValue(mutableListOf(Pair.of("key", StringValue(entry.first)), Pair.of("value", entry.second))))
        }

        return ListValue(list)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.STRUCT_ENTRIES_BUILTIN

    companion object {
        val CODEC: MapCodec<StructEntriesBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                StructValue.CODEC.fieldOf("struct").forGetter(StructEntriesBuiltin::struct),
                Arguments.CODEC.fieldOf("arguments").forGetter(StructEntriesBuiltin::arguments)
            ).apply(it, ::StructEntriesBuiltin)
        }
    }
}
