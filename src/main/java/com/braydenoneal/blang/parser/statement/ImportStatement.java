package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public record ImportStatement(Program program, List<String> identifiers) implements Statement {
    @Override
    public Statement execute() {
        program.addImport(this);
        return null;
    }

    public static Statement parse(Program program) throws Exception {
        List<String> identifiers = new ArrayList<>();
        program.expect(Type.KEYWORD, "import");

        while (program.peek().type() == Type.IDENTIFIER) {
            identifiers.add(program.next().value());

            if (program.peek().type() != Type.SEMICOLON) {
                program.expect(Type.DOT);
            }
        }

        program.expect(Type.SEMICOLON);
        return new ImportStatement(program, identifiers);
    }
}
