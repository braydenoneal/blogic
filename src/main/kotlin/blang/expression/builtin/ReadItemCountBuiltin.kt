package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.BooleanValue
import program.expression.value.IntegerValue
import program.expression.value.Value

data class ReadItemCountBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        val program = BlogicProgram.cast(program)
        val x = (arguments.integerValue(program, "x", 0)).value
        val y = (arguments.integerValue(program, "y", 1)).value
        val z = (arguments.integerValue(program, "z", 2)).value
        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 3))
        var count = 0

        val world = program.context.entity.level ?: throw RunException("World is null")

        val entityPos = program.context.pos
        val exportEntity = world.getBlockEntity(BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z))

        if (exportEntity !is BaseContainerBlockEntity) {
            throw RunException("Block at position is not a container")
        }

        exportEntity.iterator().forEachRemaining { stack ->
            val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
            val predicateResult = itemPredicate.call(program, predicateArguments).cast<BooleanValue>()

            if (predicateResult.value) {
                count += stack.count
            }
        }

        return IntegerValue(count)
    }
}
