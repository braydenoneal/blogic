package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.ItemValue
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Items


data class GetItemsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val items: MutableList<Value<*>> = ArrayList()

        for (container in program.context().entity!!.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                if (!stack.isOf(Items.AIR) && !items.contains(ItemValue(stack.item))) {
                    items.add(ItemValue(stack.item))
                }
            }
        }

        return ListValue(items)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.GET_ITEMS_BUILTIN

    companion object {
        val CODEC: MapCodec<GetItemsBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(GetItemsBuiltin::arguments)
            ).apply(it, ::GetItemsBuiltin)
        }
    }
}
