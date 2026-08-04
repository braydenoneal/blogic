package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.value.*
import program.expression.value.util.Null

object DeleteItemsBuiltin : Callable {
    context(program: Program, arguments: Arguments)
    override fun innerCall(): Value<*> {
        val program = BlogicProgram.cast(program.actionProgram)
        val itemPredicate = arguments.get<FunctionValue>("itemPredicate")
        val initialCount = arguments.getAny("count", Null.VALUE)
        var count: Int? = null

        if (initialCount is IntegerValue) {
            count = initialCount.value
        }

        val containers = program.context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                if (count != null && count <= 0) {
                    return Null.VALUE
                }

                val stack = container.getItem(slot)

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
                val predicateResult = context(predicateArguments) { itemPredicate.call().cast<BooleanValue>() }

                if (!predicateResult.value) {
                    continue
                }

                if (count != null) {
                    if (count - stack.count >= 0) {
                        count -= stack.count
                    } else {
                        stack.shrink(count)
                        return Null.VALUE
                    }
                }

                container.removeItemNoUpdate(slot)
            }
        }

        return Null.VALUE
    }
}
