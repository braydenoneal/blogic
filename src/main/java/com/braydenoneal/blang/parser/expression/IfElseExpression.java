package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

public record IfElseExpression(
        Expression condition,
        Expression expression_a,
        Expression expression_b
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> conditionValue = condition.evaluate(program);

        if (conditionValue instanceof BooleanValue booleanValue && booleanValue.value()) {
            return expression_a.evaluate(program);
        }

        return expression_b.evaluate(program);
    }

    public static Expression parse(Program program, Expression expression_a) throws Exception {
        program.expect(Type.KEYWORD, "if");
        Expression condition = Expression.parse(program);
        program.expect(Type.KEYWORD, "else");
        Expression expression_b = Expression.parse(program);
        return new IfElseExpression(condition, expression_a, expression_b);
    }
}
