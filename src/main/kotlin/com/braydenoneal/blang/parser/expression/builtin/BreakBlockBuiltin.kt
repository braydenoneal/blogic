package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BlockValue
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.entity.FakePlayer
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import java.util.Map
import kotlin.math.min


data class BreakBlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val x = arguments.integerValue(program, "x", 0).value
        val y = arguments.integerValue(program, "y", 1).value
        val z = arguments.integerValue(program, "z", 2).value
        val blockPredicate = arguments.functionValue(program, "blockPredicate", 3)
        val silkTouch = if (arguments.arguments.size > 4 || arguments.namedArguments.containsKey("silkTouch")) arguments.booleanValue(program, "silkTouch", 4).value else false

        val entityPos = program.context().pos
        val pos = BlockPos(entityPos.x + x, entityPos.y + y, entityPos.z + z)
        val world = program.context().entity!!.getWorld()

        if (world == null) {
            throw RunException("World is null")
        }

        val block = world.getBlockState(pos).block

        val predicateArguments = Arguments(mutableListOf(BlockValue(block)), Map.of())
        val predicateResult = blockPredicate.call(program, predicateArguments)

        if (predicateResult !is BooleanValue) {
            throw RunException("blockPredicate is not a predicate")
        }

        if (!predicateResult.value) {
            return BooleanValue(false)
        }

        val containers = program.context().entity!!.getConnectedContainers()
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
        for (drop in drops) {
            if (!drop.isEmpty) {
                Block.dropStack(world, pos, drop)
            }
        }

        return BooleanValue(true)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.BREAK_BLOCK_BUILTIN

    companion object {
        val CODEC: MapCodec<BreakBlockBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(BreakBlockBuiltin::arguments)
            ).apply(instance, ::BreakBlockBuiltin)
        }
    }
}
