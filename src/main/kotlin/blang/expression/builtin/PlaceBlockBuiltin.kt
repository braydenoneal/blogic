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
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.BooleanValue
import program.expression.value.Value

data class PlaceBlockBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        val program = BlogicProgram.cast(program)
        val x = (arguments.integerValue(program, "x", 0)).value
        val y = (arguments.integerValue(program, "y", 1)).value
        val z = (arguments.integerValue(program, "z", 2)).value
        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 3))

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
                val predicateResult = itemPredicate.call(program, predicateArguments).cast<BooleanValue>()

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
