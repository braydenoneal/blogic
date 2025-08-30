package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.statement.Statement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Function(List<String> arguments, List<Statement> statements) {
    public static final Codec<Function> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.STRING).fieldOf("arguments").forGetter(Function::arguments),
            Codec.list(Statement.CODEC).fieldOf("statements").forGetter(Function::statements)
    ).apply(instance, Function::new));
}
