package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.*;
import com.mojang.serialization.Codec;
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

public record BreakBlockBuiltin(List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> xValue = arguments.get(0).evaluate(program);
        Value<?> yValue = arguments.get(1).evaluate(program);
        Value<?> zValue = arguments.get(2).evaluate(program);
        Expression blockPredicateExpression = arguments.get(3);
        boolean silkTouch = arguments.size() > 4 && arguments.get(4).evaluate(program) instanceof BooleanValue booleanValue ? booleanValue.value() : false;

        if (xValue instanceof IntegerValue x &&
                yValue instanceof IntegerValue y &&
                zValue instanceof IntegerValue z &&
                blockPredicateExpression instanceof FunctionValue blockPredicate
        ) {
            BlockPos entityPos = program.context().pos();
            BlockPos pos = new BlockPos(entityPos.getX() + x.value(), entityPos.getY() + y.value(), entityPos.getZ() + z.value());
            World world = program.context().entity().getWorld();

            if (world == null) {
                throw new RunException("World is null");
            }

            Block block = world.getBlockState(pos).getBlock();

            program.newScope();
            program.getScope().set(blockPredicate.value().arguments().getFirst(), new BlockValue(block));
            Value<?> predicateResult = blockPredicate.call(program);
            program.endScope();

            if (!(predicateResult instanceof BooleanValue booleanValue && booleanValue.value())) {
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
                    for (int i = 0; i < container.size(); i++) {
                        ItemStack stack = container.getStack(i);

                        if (stack.isOf(drop.getItem()) && stack.getCount() < stack.getMaxCount()) {
                            int move = Math.min(drop.getCount(), stack.getMaxCount() - stack.getCount());

                            drop.decrement(move);
                            stack.increment(move);

                            container.setStack(i, stack);
                        }

                        if (stack.isOf(Items.AIR)) {
                            container.setStack(i, drop.copy());
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
            // Drop items in the world that there wasn't enough room for
            for (ItemStack drop : drops) {
                if (!drop.isEmpty()) {
                    Block.dropStack(world, pos, drop);
                }
            }

            return new BooleanValue(true);
        }

        throw new RunException("Invalid arguments");
    }

    public static final MapCodec<BreakBlockBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(BreakBlockBuiltin::arguments)
    ).apply(instance, BreakBlockBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.BREAK_BLOCK_BUILTIN;
    }
}
