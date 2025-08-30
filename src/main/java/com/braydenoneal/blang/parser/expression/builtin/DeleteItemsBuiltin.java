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
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public record DeleteItemsBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        FunctionValue itemPredicate = arguments.functionValue(program, "itemPredicate", 0);

        World world = program.context().entity().getWorld();

        if (world == null) {
            throw new RunException("World is null");
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

                if (((BooleanValue) predicateResult).value()) {
                    continue;
                }

                container.removeStack(slot);
            }
        }

        return Null.value();
    }

    public static final MapCodec<DeleteItemsBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(DeleteItemsBuiltin::arguments)
    ).apply(instance, DeleteItemsBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.DELETE_ITEMS_BUILTIN;
    }
}
