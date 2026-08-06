package blang.expression.builtin

import blang.Context
import program.Program
import program.expression.Arguments
import program.expression.value.Value
import program.expression.value.integer.IntegerValue

object GetItemCountBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val predicate = getPredicate()
        var count = 0

        for (container in context.entity.getConnectedContainers()) {
            container.iterator().forEachRemaining { stack ->
                if (getPredicateResult(stack.item, predicate)) {
                    count += stack.count
                }
            }
        }

        return IntegerValue(count)
    }
}
