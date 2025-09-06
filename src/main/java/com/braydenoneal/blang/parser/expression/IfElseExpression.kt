package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record IfElseExpression(
        Expression condition,
        Expression expression_a,
        Expression expression_b
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> conditionValue = condition.evaluate(program);

        if (conditionValue instanceof BooleanValue booleanValue) {
            return booleanValue.value() ? expression_a.evaluate(program) : expression_b.evaluate(program);
        }

        throw new RunException("Condition is not a boolean");
    }

    public static Expression parse(Program program, Expression expression_a) throws ParseException {
        program.expect(Type.KEYWORD, "if");
        Expression condition = Expression.parse(program);
        program.expect(Type.KEYWORD, "else");
        Expression expression_b = Expression.parse(program);
        return new IfElseExpression(condition, expression_a, expression_b);
    }

    public static final MapCodec<IfElseExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("condition").forGetter(IfElseExpression::condition),
            Expression.CODEC.fieldOf("expression_a").forGetter(IfElseExpression::expression_a),
            Expression.CODEC.fieldOf("expression_b").forGetter(IfElseExpression::expression_b)
    ).apply(instance, IfElseExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.IF_ELSE_EXPRESSION;
    }
}
