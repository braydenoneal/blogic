package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

public record Identifier(Token value) implements Expression {
    public static Identifier parse(Program program) throws Exception {
        Token token = program.getToken(0);

        if (token.type() == Type.IDENTIFIER) {
            program.offsetLocation(1);
            return new Identifier(token);
        }

        throw new Exception("Not an identifier");
    }
}
