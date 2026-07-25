package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import net.minecraft.world.item.Items
import program.Program
import program.expression.Arguments
import program.expression.value.ListValue
import program.expression.value.Value

object GetItemsBuiltin {
    fun call(
        program: Program,
        @Suppress("unused")
        arguments: Arguments,
    ): Value<*> {
        val program = BlogicProgram.cast(program)
        val items: MutableList<Value<*>> = mutableListOf()

        for (container in program.context.entity.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                if (!stack.`is`(Items.AIR) && !items.contains(ItemValue(stack.item))) {
                    items.add(ItemValue(stack.item))
                }
            }
        }

        return ListValue(items)
    }
}
