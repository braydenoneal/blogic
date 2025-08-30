package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.FunctionExpression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ItemValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public record ExportAllItemsBuiltin(List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> xValue = arguments.get(0).evaluate(program);
        Value<?> yValue = arguments.get(1).evaluate(program);
        Value<?> zValue = arguments.get(2).evaluate(program);
        Expression itemPredicateExpression = arguments.get(3);

        if (xValue instanceof IntegerValue x &&
                yValue instanceof IntegerValue y &&
                zValue instanceof IntegerValue z &&
                itemPredicateExpression instanceof FunctionExpression itemPredicate
        ) {
            World world = program.context().entity().getWorld();

            if (world == null) {
                return null;
            }

            BlockPos entityPos = program.context().pos();
            BlockEntity exportEntity = world.getBlockEntity(new BlockPos(entityPos.getX() + x.value(), entityPos.getY() + y.value(), entityPos.getZ() + z.value()));

            if (exportEntity instanceof LockableContainerBlockEntity exportContainer) {
                List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();

                for (LockableContainerBlockEntity container : containers) {
                    for (int i = 0; i < container.size(); i++) {
                        ItemStack stack = container.getStack(i);

                        program.newScope();
                        program.getScope().set(itemPredicate.arguments().getFirst(), new ItemValue(stack.getItem()));
                        Value<?> predicateResult = itemPredicate.evaluate(program);
                        program.endScope();

                        if (predicateResult instanceof BooleanValue booleanValue && booleanValue.value()) {
                            for (int j = 0; j < exportContainer.size(); j++) {
                                ItemStack exportStack = exportContainer.getStack(j);

                                if (exportStack.isOf(Items.AIR)) {
                                    container.removeStack(i);
                                    exportContainer.setStack(j, stack);
                                    break;
                                }

                                if (exportStack.isOf(stack.getItem())) {
                                    int move = Math.min(stack.getCount(), exportStack.getMaxCount() - exportStack.getCount());

                                    stack.decrement(move);
                                    exportStack.increment(move);

                                    if (stack.isEmpty()) {
                                        container.removeStack(i);
                                        exportContainer.setStack(j, exportStack);
                                        break;
                                    } else {
                                        container.setStack(i, stack);
                                        exportContainer.setStack(j, exportStack);
                                    }
                                }
                            }
                        }
                    }
                }

                return null;
            }
        }

        System.out.println("exportAllItems");
        System.out.println(xValue);
        System.out.println(yValue);
        System.out.println(zValue);
        System.out.println(itemPredicateExpression);
        return null;
    }
}
