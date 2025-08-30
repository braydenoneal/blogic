package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BlockValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public record BlockBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        return new BlockValue(Registries.BLOCK.get(Identifier.of(arguments.stringValue(program, "value", 0).value())));
    }

    public static final MapCodec<BlockBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(BlockBuiltin::arguments)
    ).apply(instance, BlockBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.BLOCK_BUILTIN;
    }
}
