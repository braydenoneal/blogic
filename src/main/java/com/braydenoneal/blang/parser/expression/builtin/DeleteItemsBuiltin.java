package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.FunctionExpression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.ItemValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public record DeleteItemsBuiltin(Program program, List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate() {
        Expression itemPredicateExpression = arguments.getFirst();

        if (itemPredicateExpression instanceof FunctionExpression itemPredicate) {
            World world = program.context().entity().getWorld();

            if (world == null) {
                return null;
            }

            List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();

            for (LockableContainerBlockEntity container : containers) {
                for (int i = 0; i < container.size(); i++) {
                    ItemStack stack = container.getStack(i);

                    program.newScope();
                    program.getScope().set(itemPredicate.arguments().getFirst(), new ItemValue(stack.getItem()));
                    Value<?> predicateResult = itemPredicate.evaluate();
                    program.endScope();

                    if (predicateResult instanceof BooleanValue booleanValue && booleanValue.value()) {
                        container.removeStack(i);
                    }
                }
            }

            return null;
        }

        System.out.println("deleteItems");
        System.out.println(itemPredicateExpression);
        return null;
    }
}
