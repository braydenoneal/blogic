package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.TagValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class TagsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val item = arguments.itemValue(program, "value", 0).value
        val tags = ArrayList<Value<*>>()

        item.defaultStack.streamTags().forEach { tags.add(TagValue(it)) }

        return ListValue(tags)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.TAGS_BUILTIN

    companion object {
        val CODEC: MapCodec<TagsBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(TagsBuiltin::arguments)
            ).apply(it, ::TagsBuiltin)
        }
    }
}
