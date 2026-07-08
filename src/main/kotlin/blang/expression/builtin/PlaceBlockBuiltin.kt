package blang.expression.builtin

import blang.expression.value.ItemValue
import net.minecraft.core.BlockPos
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.BooleanValue
import parser.expression.value.Value

data class PlaceBlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is blang.BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val x = (arguments.integerValue(program, "x", 0) ?: return null).value
        val y = (arguments.integerValue(program, "y", 1) ?: return null).value
        val z = (arguments.integerValue(program, "z", 2) ?: return null).value
        val itemPredicate = (arguments.functionValue(program, "itemPredicate", 3) ?: return null)

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
                val predicateResult = itemPredicate.call(program, predicateArguments)

                if (predicateResult !is BooleanValue) {
                    throw RunException("itemPredicate is not a predicate")
                }

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
