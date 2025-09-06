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
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import java.util.function.Consumer


data class GetItemsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val containers = program.context().entity!!.getConnectedContainers()
        val items: MutableList<Value<*>> = ArrayList()

        for (container in containers) {
            container.iterator().forEachRemaining(Consumer { stack: ItemStack ->
                if (!stack.isOf(Items.AIR)) {
                    items.add(ItemValue(stack.item))
                }
            })
        }

        return ListValue(items)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.GET_ITEMS_BUILTIN

    companion object {
        val CODEC: MapCodec<GetItemsBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(GetItemsBuiltin::arguments)
            ).apply(instance, ::GetItemsBuiltin)
        }
    }
}
