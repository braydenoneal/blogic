package blang.expression.builtin

import blang.Context
import blang.expression.value.ItemValue
import net.minecraft.world.item.Items
import program.Program
import program.expression.Arguments
import program.expression.value.ListValue
import program.expression.value.Value

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
