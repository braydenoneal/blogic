package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.ItemValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier


data class ItemBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return ItemValue(Registries.ITEM.get(Identifier.of(arguments.stringValue(program, "value", 0).value)))
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.ITEM_BUILTIN

    companion object {
        val CODEC: MapCodec<ItemBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(ItemBuiltin::arguments)
            ).apply(it, ::ItemBuiltin)
        }
    }
}
