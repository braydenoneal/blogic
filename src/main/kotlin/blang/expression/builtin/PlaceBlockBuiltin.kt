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
import program.expression.value.BooleanValue
import program.expression.value.FunctionValue
import program.expression.value.IntegerValue
import program.expression.value.Value

object PlaceBlockBuiltin {
    fun call(program: Program, arguments: Arguments): Value<*> {
        context(program) {
            val program = BlogicProgram.cast(program.actionProgram)
            val x = arguments.get<IntegerValue>("x").value
            val y = arguments.get<IntegerValue>("y").value
            val z = arguments.get<IntegerValue>("z").value
            val predicate = arguments.get<FunctionValue>("predicate")

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
}
