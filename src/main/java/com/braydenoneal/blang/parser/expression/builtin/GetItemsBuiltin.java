package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.ItemValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public record GetItemsBuiltin(List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();
        List<Value<?>> items = new ArrayList<>();

        for (LockableContainerBlockEntity container : containers) {
            container.iterator().forEachRemaining(stack -> {
                if (!stack.isOf(Items.AIR)) {
                    items.add(new ItemValue(stack.getItem()));
                }
            });
        }

        return new ListValue(items);
    }
}
