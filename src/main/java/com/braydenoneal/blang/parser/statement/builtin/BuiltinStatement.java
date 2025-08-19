package com.braydenoneal.blang.parser.statement.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.builtin.BuiltinExpression;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.List;

public class BuiltinStatement {
    public static List<Expression> parseArguments(Program program, String name) throws Exception {
        program.expect(Type.IDENTIFIER, name);
        List<Expression> arguments = BuiltinExpression.parseArguments(program);
        program.expect(Type.SEMICOLON);

        return arguments;
    }
}
