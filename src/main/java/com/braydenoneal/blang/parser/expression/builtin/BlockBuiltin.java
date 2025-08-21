package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BlockValue;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public record BlockBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();

        if (value instanceof StringValue name) {
            return new BlockValue(Registries.BLOCK.get(Identifier.of(name.value())));
        }

        System.out.println("block");
        System.out.println(value);
        return null;
    }
}
