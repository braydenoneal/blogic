package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record BreakStatement() implements Statement {
    @Override
    public Statement execute(Program program) {
        return this;
    }

    public static Statement parse(Program program) throws ParseException {
        program.expect(Type.KEYWORD, "break");
        program.expect(Type.SEMICOLON);
        return new BreakStatement();
    }

    public static final MapCodec<BreakStatement> CODEC = Codec.unit(new BreakStatement()).fieldOf("break_statement");

    @Override
    public StatementType<?> getType() {
        return StatementTypes.BREAK_STATEMENT;
    }
}
