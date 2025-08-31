package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BlockValue;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.FunctionValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public record BreakBlockBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        int x = arguments.integerValue(program, "x", 0).value();
        int y = arguments.integerValue(program, "y", 1).value();
        int z = arguments.integerValue(program, "z", 2).value();
        FunctionValue blockPredicate = arguments.functionValue(program, "blockPredicate", 3);
        boolean silkTouch = arguments.arguments().size() > 4 ||
                arguments.namedArguments().containsKey("silkTouch") ?
                arguments.booleanValue(program, "silkTouch", 4).value() : false;

        BlockPos entityPos = program.context().pos();
        BlockPos pos = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
        World world = program.context().entity().getWorld();

        if (world == null) {
            throw new RunException("World is null");
        }

        Block block = world.getBlockState(pos).getBlock();

        Arguments predicateArguments = new Arguments(List.of(new BlockValue(block)), Map.of());
        Value<?> predicateResult = blockPredicate.call(program, predicateArguments);

        if (!(predicateResult instanceof BooleanValue)) {
            throw new RunException("blockPredicate is not a predicate");
        }

        if (!((BooleanValue) predicateResult).value()) {
            return new BooleanValue(false);
        }

        List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();
        ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE);

        if (silkTouch) {
            Registry<Enchantment> registry = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            RegistryEntry<Enchantment> enchantment = registry.getEntry(registry.get(Enchantments.SILK_TOUCH));
            tool.addEnchantment(enchantment, 1);
        }

        List<ItemStack> drops = Block.getDroppedStacks(world.getBlockState(pos), (ServerWorld) world, pos, world.getBlockEntity(pos), FakePlayer.get((ServerWorld) world), tool);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());

        for (ItemStack drop : drops) {
            for (LockableContainerBlockEntity container : containers) {
                for (int slot = 0; slot < container.size(); slot++) {
                    ItemStack stack = container.getStack(slot);

                    if (stack.isOf(drop.getItem()) && stack.getCount() < stack.getMaxCount()) {
                        int move = Math.min(drop.getCount(), stack.getMaxCount() - stack.getCount());

                        drop.decrement(move);
                        stack.increment(move);

                        container.setStack(slot, stack);
                    }

                    if (stack.isOf(Items.AIR)) {
                        container.setStack(slot, drop.copy());
                        drop.setCount(0);
                    }

                    if (drop.isEmpty()) {
                        break;
                    }
                }

                if (drop.isEmpty()) {
                    break;
                }
            }
        }

        // TODO: Only break if there is enough room for the drops?
        for (ItemStack drop : drops) {
            if (!drop.isEmpty()) {
                Block.dropStack(world, pos, drop);
            }
        }

        return new BooleanValue(true);
    }

    public static final MapCodec<BreakBlockBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(BreakBlockBuiltin::arguments)
    ).apply(instance, BreakBlockBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.BREAK_BLOCK_BUILTIN;
    }
}
