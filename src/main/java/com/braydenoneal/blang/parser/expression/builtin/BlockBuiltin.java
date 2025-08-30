package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BlockValue;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public record BlockBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = expression.evaluate(program);

        if (value instanceof StringValue name) {
            return new BlockValue(Registries.BLOCK.get(Identifier.of(name.value())));
        }

        System.out.println("block");
        System.out.println(value);
        return null;
    }

    public static final MapCodec<BlockBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("expression").forGetter(BlockBuiltin::expression)
    ).apply(instance, BlockBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.BLOCK_BUILTIN;
    }
}
