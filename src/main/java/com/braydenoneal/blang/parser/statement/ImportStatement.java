package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public record ImportStatement(List<String> identifiers) {
    public static List<ImportStatement> parse(Program program) throws Exception {
        List<ImportStatement> imports = new ArrayList<>();

        while (program.peekIs(Type.KEYWORD, "import")) {
            List<String> identifiers = new ArrayList<>();
            program.expect(Type.KEYWORD, "import");

            while (program.peek().type() == Type.IDENTIFIER) {
                identifiers.add(program.next().value());

                if (program.peek().type() != Type.SEMICOLON) {
                    program.expect(Type.DOT);
                }
            }

            program.expect(Type.SEMICOLON);
            imports.add(new ImportStatement(identifiers));
        }

        return imports;
    }
}
