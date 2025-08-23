package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.List;

public interface Statement {
    Statement execute();

    static Statement parse(Program program) throws Exception {
        Token token = program.peek();

        if (token.type() == Type.KEYWORD) {
            switch (token.value()) {
                case "import" -> {
                    return ImportStatement.parse(program);
                }
                case "fn" -> {
                    return FunctionDeclaration.parse(program);
                }
                case "if" -> {
                    return IfStatement.parse(program);
                }
                case "while" -> {
                    return WhileStatement.parse(program);
                }
                case "for" -> {
                    return ForStatement.parse(program);
                }
                case "break" -> {
                    return BreakStatement.parse(program);
                }
                case "continue" -> {
                    return ContinueStatement.parse(program);
                }
                case "return" -> {
                    return ReturnStatement.parse(program);
                }
            }
        } else {
            return ExpressionStatement.parse(program);
        }

        return null;
    }

    static Statement runStatements(List<Statement> statements) {
        for (Statement statement : statements) {
            Statement statementResult = statement.execute();

            if (statementResult instanceof ReturnStatement ||
                    statementResult instanceof BreakStatement ||
                    statementResult instanceof ContinueStatement
            ) {
                return statementResult;
            }
        }

        return null;
    }
}
