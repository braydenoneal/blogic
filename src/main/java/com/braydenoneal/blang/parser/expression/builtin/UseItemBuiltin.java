package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.FunctionExpression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ItemValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public record UseItemBuiltin(Program program, List<Expression> arguments) implements Expression {
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

            List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();

            for (LockableContainerBlockEntity container : containers) {
                for (int i = 0; i < container.size(); i++) {
                    ItemStack stack = container.getStack(i);

                    if (stack.isOf(Items.AIR)) {
                        continue;
                    }

                    program.newScope();
                    program.getScope().set(itemPredicate.arguments().getFirst(), new ItemValue(stack.getItem()));
                    Value<?> predicateResult = itemPredicate.evaluate();
                    program.endScope();

                    if (predicateResult instanceof BooleanValue booleanValue && booleanValue.value()) {
                        stack.useOnBlock(new ItemUsageContext(
                                world,
                                FakePlayer.get((ServerWorld) world),
                                Hand.MAIN_HAND,
                                stack,
                                new BlockHitResult(
                                        new Vec3d(pos.getX(), pos.getY(), pos.getZ()),
                                        program.context().entity().getFacing(),
                                        pos,
                                        false
                                )
                        ));

                        container.setStack(i, stack);
                        return null;
                    }
                }
            }

            return null;
        }

        System.out.println("useItem");
        System.out.println(xValue);
        System.out.println(yValue);
        System.out.println(zValue);
        System.out.println(itemPredicateExpression);
        return null;
    }
}
