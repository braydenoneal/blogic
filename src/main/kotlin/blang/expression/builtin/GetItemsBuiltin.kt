package blang.expression.builtin

import net.minecraft.item.Items
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.ListValue
import parser.expression.value.Value

data class GetItemsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        if (program !is blang.BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val items: MutableList<Value<*>> = ArrayList()

        for (container in program.context.entity.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                if (!stack.isOf(Items.AIR) && !items.contains(_root_ide_package_.blang.expression.value.ItemValue(stack.item))) {
                    items.add(_root_ide_package_.blang.expression.value.ItemValue(stack.item))
                }
            }
        }

        return ListValue(items)
    }
}
