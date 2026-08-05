package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.ItemValue
import net.minecraft.core.BlockPos
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.value.*

object PlaceBlockBuiltin : Callable {
    context(program: Program, arguments: Arguments)
    override fun innerCall(): Value<*> {
        val program = BlogicProgram.cast(program.actionProgram)
        val x = get<IntegerValue>("x").value
        val y = get<IntegerValue>("y").value
        val z = get<IntegerValue>("z").value
        val predicate = get<FunctionValue>("predicate")

        val entityPos = program.context.pos
        val pos = BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z)
        val world = program.context.entity.level ?: throw RunException("World is null")

        if (world.getBlockState(pos).block !== Blocks.AIR) {
            return BooleanValue(false)
        }

        val containers = program.context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                val stack = container.getItem(slot)

                if (stack.`is`(Items.AIR)) {
                    continue
                }

                val predicateArguments = Arguments(mutableListOf(ItemValue(stack.item)), mutableMapOf())
                val predicateResult = context(predicateArguments) { predicate.call().cast<BooleanValue>() }

                if (!predicateResult.value) {
                    continue
                }

                for (entry in BlockItem.BY_BLOCK.entries) {
                    if (!stack.`is`(entry.value)) {
                        continue
                    }

                    stack.shrink(1)

                    if (stack.isEmpty) {
                        container.setItem(slot, ItemStack.EMPTY)
                    } else {
                        container.setItem(slot, stack)
                    }

                    world.setBlockAndUpdate(pos, entry.key.defaultBlockState())
                    return BooleanValue(true)
                }
            }
        }

        return BooleanValue(false)
    }
}
