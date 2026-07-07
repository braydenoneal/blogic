package blang.expression.builtin

import blang.expression.value.BlockValue
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import parser.Program
import parser.RunException
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.BooleanValue
import parser.expression.value.Value
import kotlin.math.min

data class BreakBlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        if (program !is blang.BlogicProgram) {
            throw RunException("Program is not a BlogicProgram")
        }

        val x = (arguments.integerValue(program, "x", 0) ?: return null).value
        val y = (arguments.integerValue(program, "y", 1) ?: return null).value
        val z = (arguments.integerValue(program, "z", 2) ?: return null).value
        val blockPredicate = (arguments.functionValue(program, "blockPredicate", 3) ?: return null)
        val silkTouch = if (arguments.arguments.size > 4 || arguments.namedArguments.containsKey("silkTouch")) (arguments.booleanValue(program, "silkTouch", 4) ?: return null).value else false

        val entityPos = program.context.pos
        val pos = BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z)
        val world = program.context.entity.getWorld() ?: throw RunException("World is null")

        val block = world.getBlockState(pos).block

        val predicateArguments = Arguments(mutableListOf(BlockValue(block)), mutableMapOf())
        val predicateResult = blockPredicate.call(program, predicateArguments)

        if (predicateResult !is BooleanValue) {
            throw RunException("blockPredicate is not a predicate")
        }

        if (!predicateResult.value) {
            return BooleanValue(false)
        }

        val containers = program.context.entity.getConnectedContainers()
        val tool = ItemStack(Items.DIAMOND_PICKAXE)

        if (silkTouch) {
            val registry = world.registryManager.getOrThrow(RegistryKeys.ENCHANTMENT)
            val enchantment = registry.getEntry(registry.get(Enchantments.SILK_TOUCH))
            tool.addEnchantment(enchantment, 1)
        }

        val drops = Block.getDroppedStacks(world.getBlockState(pos), world as ServerWorld, pos, world.getBlockEntity(pos), FakePlayer.get(world), tool)
        world.setBlockState(pos, Blocks.AIR.defaultState)

        for (drop in drops) {
            for (container in containers) {
                for (slot in 0..<container.size()) {
                    val stack = container.getStack(slot)

                    if (stack.isOf(drop.item) && stack.count < stack.maxCount) {
                        val move = min(drop.count, stack.maxCount - stack.count)

                        drop.decrement(move)
                        stack.increment(move)

                        container.setStack(slot, stack)
                    }

                    if (stack.isOf(Items.AIR)) {
                        container.setStack(slot, drop.copy())
                        drop.count = 0
                    }

                    if (drop.isEmpty) {
                        break
                    }
                }

                if (drop.isEmpty) {
                    break
                }
            }
        }

        // TODO: Only break if there is enough room for the drops
//        for (drop in drops) {
//            if (!drop.isEmpty) {
//                Block.dropStack(world, pos, drop)
//            }
//        }

        return BooleanValue(true)
    }
}
