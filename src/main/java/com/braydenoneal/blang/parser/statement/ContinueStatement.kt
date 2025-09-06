package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record ContinueStatement() implements Statement {
    @Override
    public Statement execute(Program program) {
        return this;
    }

    public static Statement parse(Program program) throws ParseException {
        program.expect(Type.KEYWORD, "continue");
        program.expect(Type.SEMICOLON);
        return new ContinueStatement();
    }

    public static final MapCodec<ContinueStatement> CODEC = Codec.unit(new ContinueStatement()).fieldOf("continue_statement");

    @Override
    public StatementType<?> getType() {
        return StatementTypes.CONTINUE_STATEMENT;
    }
}
