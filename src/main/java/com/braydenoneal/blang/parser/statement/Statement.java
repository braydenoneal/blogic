package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

public interface Statement {
    Value<?> execute();

    static Statement parse(Program program) throws Exception {
        Token token = program.peek();

        if (token.type() == Type.KEYWORD) {
            switch (token.value()) {
                case "fn" -> {
                    return FunctionDeclaration.parse(program);
                }
                case "if" -> {
                    return IfStatement.parse(program);
                }
                case "while" -> {
                    return WhileStatement.parse(program);
                }
                case "print" -> {
                    return PrintStatement.parse(program);
                }
                case "return" -> {
                    return ReturnStatement.parse(program);
                }
            }
        } else if (token.type() == Type.IDENTIFIER) {
            return AssignmentStatement.parse(program);
        }

        return null;
    }
}
