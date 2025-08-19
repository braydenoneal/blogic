package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

public record PrintStatement(
        Expression expression
) implements Statement {
    @Override
    public Value<?> execute() {
        Value<?> value = expression.evaluate();
        String string = value.toString();

        if (value instanceof StringValue) {
            string = string.substring(1, string.length() - 1);
        }

        System.out.println(string);
        return null;
    }

    public static Statement parse(Program program) throws Exception {
        program.expect(Type.KEYWORD, "print");
        program.expect(Type.PARENTHESIS, "(");

        Expression expression = Expression.parse(program);

        program.expect(Type.PARENTHESIS, ")");
        program.expect(Type.SEMICOLON);

        return new PrintStatement(expression);
    }
}
