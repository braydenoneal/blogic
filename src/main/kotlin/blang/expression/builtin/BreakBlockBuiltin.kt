package blang.expression.builtin

import blang.BlogicProgram
import blang.expression.value.BlockValue
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import program.Program
import program.RunException
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.BooleanValue
import program.expression.value.FunctionValue
import program.expression.value.IntegerValue
import program.expression.value.Value
import kotlin.math.min

data class BreakBlockBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun innerEvaluate(program: Program): Value<*> {
        val program = BlogicProgram.cast(program)
        val x = arguments.get<IntegerValue>(program, "x").value
        val y = arguments.get<IntegerValue>(program, "y").value
        val z = arguments.get<IntegerValue>(program, "z").value
        val predicate = arguments.get<FunctionValue>(program, "predicate")
        val silkTouch = arguments.get<BooleanValue>(program, "silkTouch", BooleanValue(false)).value

        val entityPos = program.context.pos
        val pos = BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z)
        val world = program.context.entity.level ?: throw RunException("World is null")

        val block = world.getBlockState(pos).block

        val predicateArguments = Arguments(mutableListOf(BlockValue(block)), mutableMapOf())
        val predicateResult = predicate.call(program, predicateArguments).cast<BooleanValue>()

        if (!predicateResult.value) {
            return BooleanValue(false)
        }

        val containers = program.context.entity.getConnectedContainers()
        val tool = ItemStack(Items.DIAMOND_PICKAXE)

        if (silkTouch) {
            val registry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
            val enchantment = registry.wrapAsHolder(registry.getValue(Enchantments.SILK_TOUCH)!!)
            tool.enchant(enchantment, 1)
        }

        val drops = Block.getDrops(world.getBlockState(pos), world as ServerLevel, pos, world.getBlockEntity(pos), FakePlayer.get(world), tool)
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())

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

        return BooleanValue(true)
    }
}
