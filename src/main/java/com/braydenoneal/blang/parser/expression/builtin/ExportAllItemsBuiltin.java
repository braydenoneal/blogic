package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.*;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public record ExportAllItemsBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        int x = arguments.integerValue(program, "x", 0).value();
        int y = arguments.integerValue(program, "y", 1).value();
        int z = arguments.integerValue(program, "z", 2).value();
        FunctionValue itemPredicate = arguments.functionValue(program, "itemPredicate", 3);

        World world = program.context().entity().getWorld();

        if (world == null) {
            throw new RunException("World is null");
        }

        BlockPos entityPos = program.context().pos();
        BlockEntity exportEntity = world.getBlockEntity(new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z));

        if (!(exportEntity instanceof LockableContainerBlockEntity exportContainer)) {
            throw new RunException("Block at position is not a container");
        }

        List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();

        for (LockableContainerBlockEntity container : containers) {
            for (int slot = 0; slot < container.size(); slot++) {
                ItemStack stack = container.getStack(slot);

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

                for (int exportSlot = 0; exportSlot < exportContainer.size(); exportSlot++) {
                    ItemStack exportStack = exportContainer.getStack(exportSlot);

                    if (exportStack.isOf(Items.AIR)) {
                        container.removeStack(slot);
                        exportContainer.setStack(exportSlot, stack);
                        break;
                    }

                    if (!exportStack.isOf(stack.getItem())) {
                        continue;
                    }

                    int move = Math.min(stack.getCount(), exportStack.getMaxCount() - exportStack.getCount());

                    stack.decrement(move);
                    exportStack.increment(move);

                    if (stack.isEmpty()) {
                        container.removeStack(slot);
                        exportContainer.setStack(exportSlot, exportStack);
                        break;
                    }

                    container.setStack(slot, stack);
                    exportContainer.setStack(exportSlot, exportStack);
                }
            }
        }

        return Null.value();
    }

    public static final MapCodec<ExportAllItemsBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(ExportAllItemsBuiltin::arguments)
    ).apply(instance, ExportAllItemsBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.EXPORT_ALL_ITEMS_BUILTIN;
    }
}
