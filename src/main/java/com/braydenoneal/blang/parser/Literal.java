package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.tokenizer.Token;

public record Literal(Token value) implements Expression {
    public Literal parse(Program program) throws Exception {
        Token token = program.getToken(0);

//        if (token instanceof QuoteToken || (token instanceof NonIdentifierToken(Type type) && Type.isLiteral(type))) {
//            return new Literal(token);
//        }

        throw new Exception("Not a literal");
    }
}
