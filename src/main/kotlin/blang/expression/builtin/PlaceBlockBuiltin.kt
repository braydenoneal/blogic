package blang.expression.builtin

import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.BlockPos
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
        val world = program.context.entity.getWorld() ?: throw RunException("World is null")

        if (world.getBlockState(pos).block !== Blocks.AIR) {
            return BooleanValue(false)
        }

        val containers = program.context.entity.getConnectedContainers()

        for (container in containers) {
            for (slot in 0..<container.size()) {
                val stack = container.getStack(slot)

                if (stack.isOf(Items.AIR)) {
                    continue
                }

                val predicateArguments = Arguments(mutableListOf(_root_ide_package_.blang.expression.value.ItemValue(stack.item)), mutableMapOf())
                val predicateResult = itemPredicate.call(program, predicateArguments)

                if (predicateResult !is BooleanValue) {
                    throw RunException("itemPredicate is not a predicate")
                }

                if (!predicateResult.value) {
                    continue
                }

                for (entry in BlockItem.BLOCK_ITEMS.entries) {
                    if (!stack.isOf(entry.value)) {
                        continue
                    }

                    stack.decrement(1)

                    if (stack.isEmpty) {
                        container.setStack(slot, ItemStack.EMPTY)
                    } else {
                        container.setStack(slot, stack)
                    }

                    world.setBlockState(pos, entry.key.defaultState)
                    return BooleanValue(true)
                }
            }
        }

        return BooleanValue(false)
    }
}
