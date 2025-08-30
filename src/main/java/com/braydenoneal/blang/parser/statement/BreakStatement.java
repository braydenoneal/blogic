package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Type;

public record BreakStatement() implements Statement {
    @Override
    public Statement execute(Program program) {
        return this;
    }

    public static Statement parse(Program program) throws Exception {
        program.expect(Type.KEYWORD, "break");
        program.expect(Type.SEMICOLON);
        return new BreakStatement();
    }
}
