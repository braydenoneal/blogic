package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.builtin.Builtin
import program.expression.value.*

data class DeleteItemsBuiltin(override val arguments: Arguments) : Builtin(arguments) {
    override fun innerEvaluate(program: Program): Value<*> {
        val program = BlogicProgram.cast(program)
        val itemPredicate = arguments.get<FunctionValue>(program, "itemPredicate")
        val initialCount = arguments.getAny(program, "count", Null.VALUE)
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
                val predicateResult = itemPredicate.call(program, predicateArguments).cast<BooleanValue>()

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
