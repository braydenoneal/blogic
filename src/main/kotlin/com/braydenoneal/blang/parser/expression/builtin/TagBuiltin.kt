package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.TagValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


data class TagBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return TagValue(TagKey.of(RegistryKeys.ITEM, Identifier.of(arguments.stringValue(program, "value", 0).value)))
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.TAG_BUILTIN

    companion object {
        val CODEC: MapCodec<TagBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(TagBuiltin::arguments)
            ).apply(it, ::TagBuiltin)
        }
    }
}
