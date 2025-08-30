package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Type;

public record ContinueStatement() implements Statement {
    @Override
    public Statement execute(Program program) {
        return this;
    }

    public static Statement parse(Program program) throws Exception {
        program.expect(Type.KEYWORD, "continue");
        program.expect(Type.SEMICOLON);
        return new ContinueStatement();
    }
}
