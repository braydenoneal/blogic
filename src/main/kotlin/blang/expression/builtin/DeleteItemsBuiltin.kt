package blang.expression.builtin

import blang.Context
import program.Program
import program.expression.Arguments
import program.expression.value.IntegerValue
import program.expression.value.Value
import program.expression.value.getNullable
import program.expression.value.util.Null

object DeleteItemsBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val predicate = getPredicate()
        var count = getNullable<IntegerValue>("count")?.value

        val containers = context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                if (count != null && count <= 0) {
                    return Null.VALUE
                }

                val stack = container.getItem(slot)

                if (!getPredicateResult(stack.item, predicate)) {
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
