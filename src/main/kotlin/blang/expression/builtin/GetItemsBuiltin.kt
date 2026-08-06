package blang.expression.builtin

import blang.Context
import blang.expression.value.item.ItemValue
import net.minecraft.world.item.Items
import program.Program
import program.expression.Arguments
import program.expression.value.Value
import program.expression.value.list.ListValue

object GetItemsBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val items: MutableList<Value<*>> = mutableListOf()

        for (container in context.entity.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                if (!stack.`is`(Items.AIR) && !items.contains(ItemValue(stack.item))) {
                    items.add(ItemValue(stack.item))
                }
            }
        }

        return ListValue(items)
    }
}
