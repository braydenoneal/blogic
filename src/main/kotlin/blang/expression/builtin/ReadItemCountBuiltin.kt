package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.value.*

object ReadItemCountBuiltin : Callable {
    context(program: Program, arguments: Arguments)
    override fun innerCall(): Value<*> {
        val program = BlogicProgram.cast(program.actionProgram)
        val x = get<IntegerValue>("x").value
        val y = get<IntegerValue>("y").value
        val z = get<IntegerValue>("z").value
        val predicate = get<FunctionValue>("predicate")
        var count = 0

        val world = program.context.entity.level ?: throw RunException("World is null")

        val entityPos = program.context.pos
        val exportEntity = world.getBlockEntity(BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z))

        if (exportEntity !is BaseContainerBlockEntity) {
            throw RunException("Block at position is not a container")
        }

        exportEntity.iterator().forEachRemaining { stack ->
            val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
            val predicateResult = context(predicateArguments) { predicate.call().cast<BooleanValue>() }

            if (predicateResult.value) {
                count += stack.count
            }
        }

        return IntegerValue(count)
    }
}
