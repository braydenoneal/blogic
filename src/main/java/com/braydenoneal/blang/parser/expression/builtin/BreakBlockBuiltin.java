package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.FunctionExpression;
import com.braydenoneal.blang.parser.expression.value.BlockValue;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public record BreakBlockBuiltin(Program program, List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> xValue = arguments.get(0).evaluate();
        Value<?> yValue = arguments.get(1).evaluate();
        Value<?> zValue = arguments.get(2).evaluate();
        Expression itemPredicateExpression = arguments.get(3);

        if (xValue instanceof IntegerValue x &&
                yValue instanceof IntegerValue y &&
                zValue instanceof IntegerValue z &&
                itemPredicateExpression instanceof FunctionExpression itemPredicate
        ) {
            BlockPos entityPos = program.context().pos();
            BlockPos pos = new BlockPos(entityPos.getX() + x.value(), entityPos.getY() + y.value(), entityPos.getZ() + z.value());
            World world = program.context().entity().getWorld();

            if (world == null) {
                return null;
            }

            Block block = world.getBlockState(pos).getBlock();

            program.newScope();
            program.getScope().set(itemPredicate.arguments().getFirst(), new BlockValue(block));
            Value<?> predicateResult = itemPredicate.evaluate();
            program.endScope();

            if (!(predicateResult instanceof BooleanValue booleanValue && booleanValue.value())) {
                return null;
            }

            List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();

            for (LockableContainerBlockEntity container : containers) {
                for (int i = 0; i < container.size(); i++) {
                    ItemStack stack = container.getStack(i);

                    if (stack.isOf(block.asItem()) && stack.getCount() < stack.getMaxCount()) {
                        stack.increment(1);
                        container.setStack(i, stack);
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        return null;
                    }

                    if (stack.isOf(Items.AIR)) {
                        container.setStack(i, new ItemStack(block.asItem()));
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        return null;
                    }
                }
            }

            return null;
        }

        System.out.println("breakBlock");
        System.out.println(xValue);
        System.out.println(yValue);
        System.out.println(zValue);
        return null;
    }
}
