package com.braydenoneal.blang.wrapper.expression.builtin

import com.braydenoneal.blang.wrapper.BlogicProgram
import com.braydenoneal.blang.wrapper.expression.value.ItemValue
import net.minecraft.item.Items
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.ListValue
import parser.expression.value.Value

data class GetItemsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        if (program !is BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val items: MutableList<Value<*>> = ArrayList()

        for (container in program.context.entity.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                if (!stack.isOf(Items.AIR) && !items.contains(ItemValue(stack.item))) {
                    items.add(ItemValue(stack.item))
                }
            }
        }

        return ListValue(items)
    }
}
