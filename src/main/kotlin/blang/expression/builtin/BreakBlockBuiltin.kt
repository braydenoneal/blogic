package blang.expression.builtin

import blang.Context
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import program.Program
import program.expression.Arguments
import program.expression.value.BooleanValue
import program.expression.value.Value
import program.expression.value.get
import kotlin.math.min

object BreakBlockBuiltin : BlogicBuiltin() {
    context(program: Program, arguments: Arguments, context: Context)
    override fun blogicCall(): Value<*> {
        val pos = getBlockPos()
        val predicate = getPredicate()
        val silkTouch = get<BooleanValue>("silkTouch", BooleanValue(false)).value

        val level = getLevel()
        val block = level.getBlockState(pos).block

        if (!getPredicateResult(block, predicate)) {
            return BooleanValue(false)
        }

        val containers = context.entity.getConnectedContainers()
        val tool = ItemStack(Items.DIAMOND_PICKAXE)

        if (silkTouch) {
            val registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
            val enchantment = registry.wrapAsHolder(registry.getValue(Enchantments.SILK_TOUCH)!!)
            tool.enchant(enchantment, 1)
        }

        val drops = Block.getDrops(level.getBlockState(pos), level as ServerLevel, pos, level.getBlockEntity(pos), FakePlayer.get(level), tool)
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())

        for (drop in drops) {
            for (container in containers) {
                for (slot in 0..<container.containerSize) {
                    val stack = container.getItem(slot)

                    if (stack.`is`(drop.item) && stack.count < stack.maxStackSize) {
                        val move = min(drop.count, stack.maxStackSize - stack.count)

                        drop.shrink(move)
                        stack.grow(move)

                        container.setItem(slot, stack)
                    }

                    if (stack.`is`(Items.AIR)) {
                        container.setItem(slot, drop.copy())
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

        // TODO: Return the drops as a list of item stacks
        return BooleanValue(true)
    }
}
