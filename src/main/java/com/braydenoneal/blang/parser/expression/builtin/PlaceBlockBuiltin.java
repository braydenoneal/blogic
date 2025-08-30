package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.FunctionValue;
import com.braydenoneal.blang.parser.expression.value.ItemValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public record PlaceBlockBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        int x = arguments.integerValue(program, "x", 0).value();
        int y = arguments.integerValue(program, "y", 1).value();
        int z = arguments.integerValue(program, "z", 2).value();
        FunctionValue itemPredicate = arguments.functionValue(program, "itemPredicate", 3);

        BlockPos entityPos = program.context().pos();
        BlockPos pos = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
        World world = program.context().entity().getWorld();

        if (world == null) {
            throw new RunException("World is null");
        }

        if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
            return new BooleanValue(false);
        }

        List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();

        for (LockableContainerBlockEntity container : containers) {
            for (int slot = 0; slot < container.size(); slot++) {
                ItemStack stack = container.getStack(slot);

                if (stack.isOf(Items.AIR)) {
                    continue;
                }

                program.newScope();
                program.getScope().set(itemPredicate.value().arguments().getFirst(), new ItemValue(stack.getItem()));
                Value<?> predicateResult = itemPredicate.call(program);
                program.endScope();

                if (!(predicateResult instanceof BooleanValue)) {
                    throw new RunException("itemPredicate is not a predicate");
                }

                if (!((BooleanValue) predicateResult).value()) {
                    continue;
                }

                for (Map.Entry<Block, Item> entry : BlockItem.BLOCK_ITEMS.entrySet()) {
                    if (!stack.isOf(entry.getValue())) {
                        continue;
                    }

                    stack.decrement(1);

                    if (stack.isEmpty()) {
                        container.setStack(slot, ItemStack.EMPTY);
                    } else {
                        container.setStack(slot, stack);
                    }

                    world.setBlockState(pos, entry.getKey().getDefaultState());
                    return new BooleanValue(true);
                }
            }
        }

        return new BooleanValue(false);
    }

    public static final MapCodec<PlaceBlockBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(PlaceBlockBuiltin::arguments)
    ).apply(instance, PlaceBlockBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.PLACE_BLOCK_BUILTIN;
    }
}
