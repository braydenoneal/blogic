package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public record ImportStatement(List<String> identifiers) implements Statement {
    @Override
    public Statement execute(Program program) {
        program.addImport(this);
        return this;
    }

    public static Statement parse(Program program) throws ParseException {
        List<String> identifiers = new ArrayList<>();
        program.expect(Type.KEYWORD, "import");

        while (program.peek().type() == Type.IDENTIFIER) {
            identifiers.add(program.next().value());

            if (program.peek().type() != Type.SEMICOLON) {
                program.expect(Type.DOT);
            }
        }

        program.expect(Type.SEMICOLON);
        return new ImportStatement(identifiers);
    }

    public static final MapCodec<ImportStatement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Codec.STRING).fieldOf("identifiers").forGetter(ImportStatement::identifiers)
    ).apply(instance, ImportStatement::new));

    @Override
    public StatementType<?> getType() {
        return StatementTypes.IMPORT_STATEMENT;
    }
}
