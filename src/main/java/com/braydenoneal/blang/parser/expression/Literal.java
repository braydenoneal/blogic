package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Token;

public record Literal(Token value) implements Expression {
    public static Literal parse(Program program) throws Exception {
        Token token = program.getToken(0);

        return switch (token.type()) {
            case BOOLEAN, QUOTE, NUMBER -> {
                program.offsetLocation(1);
                yield new Literal(token);
            }
            default -> throw new Exception("Not a literal");
        };
    }
}
