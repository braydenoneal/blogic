package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;

import java.util.List;

public interface Statement {
    Statement execute(Program program);

    static Statement parse(Program program) throws ParseException {
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

        throw new ParseException("Unrecognized statement at " + token);
    }

    static Statement runStatements(Program program, List<Statement> statements) {
        for (Statement statement : statements) {
            Statement statementResult = statement.execute(program);

            if (statementResult instanceof ReturnStatement ||
                    statementResult instanceof BreakStatement ||
                    statementResult instanceof ContinueStatement
            ) {
                return statementResult;
            }
        }

        return null;
    }

    StatementType<?> getType();

    Codec<Statement> CODEC = StatementType.REGISTRY.getCodec().dispatch("type", Statement::getType, StatementType::codec);
}
