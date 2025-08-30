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
                itemPredicateExpression instanceof FunctionValue itemPredicate
        ) {
            World world = program.context().entity().getWorld();

            if (world == null) {
                throw new RunException("World is null");
            }

            BlockPos entityPos = program.context().pos();
            BlockEntity exportEntity = world.getBlockEntity(new BlockPos(entityPos.getX() + x.value(), entityPos.getY() + y.value(), entityPos.getZ() + z.value()));

            if (exportEntity instanceof LockableContainerBlockEntity exportContainer) {
                List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();

                for (LockableContainerBlockEntity container : containers) {
                    for (int i = 0; i < container.size(); i++) {
                        ItemStack stack = container.getStack(i);

                        program.newScope();
                        program.getScope().set(itemPredicate.value().arguments().getFirst(), new ItemValue(stack.getItem()));
                        Value<?> predicateResult = itemPredicate.call(program);
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

                return Null.value();
            }
        }

        throw new RunException("Invalid arguments");
    }

    public static final MapCodec<ExportAllItemsBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(ExportAllItemsBuiltin::arguments)
    ).apply(instance, ExportAllItemsBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.EXPORT_ALL_ITEMS_BUILTIN;
    }
}
