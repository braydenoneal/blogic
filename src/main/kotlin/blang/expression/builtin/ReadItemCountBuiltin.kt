package blang.expression.builtin

import blang.Context
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.value.IntegerValue
import program.expression.value.Value

object ReadItemCountBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val pos = getBlockPos()
        val predicate = getPredicate()
        var count = 0

        val exportEntity = getLevel().getBlockEntity(pos)

        if (exportEntity !is BaseContainerBlockEntity) {
            throw RunException("Block at position is not a container", arguments.span)
        }

        exportEntity.iterator().forEachRemaining { stack ->
            if (getPredicateResult(stack.item, predicate)) {
                count += stack.count
            }
        }

        return IntegerValue(count)
    }
}
