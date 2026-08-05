package blang.expression.builtin

import blang.Context
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import program.Program
import program.expression.Arguments
import program.expression.value.BooleanValue
import program.expression.value.Value

object PlaceBlockBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val pos = getBlockPos()
        val predicate = getPredicate()

        val world = getLevel()

        if (world.getBlockState(pos).block !== Blocks.AIR) {
            return BooleanValue(false)
        }

        val containers = context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.containerSize) {
                val stack = container.getItem(slot)

                if (stack.`is`(Items.AIR)) {
                    continue
                }

                if (!getPredicateResult(stack.item, predicate)) {
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
