package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ExpressionStatement(Expression expression) implements Statement {
    @Override
    public Statement execute(Program program) {
        expression.evaluate(program);
        return this;
    }

    public static Statement parse(Program program) throws Exception {
        Expression expression = Expression.parse(program);
        program.expect(Type.SEMICOLON);
        return new ExpressionStatement(expression);
    }

    public static final MapCodec<ExpressionStatement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("expression").forGetter(ExpressionStatement::expression)
    ).apply(instance, ExpressionStatement::new));

    @Override
    public StatementType<?> getType() {
        return StatementTypes.EXPRESSION_STATEMENT;
    }
}
