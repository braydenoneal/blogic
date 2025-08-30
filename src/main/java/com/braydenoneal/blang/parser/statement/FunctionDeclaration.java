package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public record FunctionDeclaration(
        String name,
        List<String> arguments,
        List<Statement> statements
) implements Statement {
    @Override
    public Statement execute(Program program) {
        return null;
    }

    public Value<?> call(Program program) {
        Value<?> returnValue = null;
        Statement statement = Statement.runStatements(program, statements);

        if (statement instanceof ReturnStatement returnStatement) {
            returnValue = returnStatement.returnValue(program);
        }

        return returnValue;
    }

    public static Statement parse(Program program) throws Exception {
        List<String> arguments = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();

        program.expect(Type.KEYWORD, "fn");
        String name = program.expect(Type.IDENTIFIER);
        program.expect(Type.PARENTHESIS, "(");

        while (!program.peekIs(Type.PARENTHESIS, ")")) {
            arguments.add(program.expect(Type.IDENTIFIER));

            if (!program.peekIs(Type.PARENTHESIS, ")")) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.PARENTHESIS, ")");
        program.expect(Type.CURLY_BRACE, "{");

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");

        FunctionDeclaration functionDeclaration = new FunctionDeclaration(name, arguments, statements);
        program.addFunction(name, functionDeclaration);
        return functionDeclaration;
    }

    public static final MapCodec<FunctionDeclaration> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(FunctionDeclaration::name),
            Codec.list(Codec.STRING).fieldOf("arguments").forGetter(FunctionDeclaration::arguments),
            Codec.list(Statement.CODEC).fieldOf("statements").forGetter(FunctionDeclaration::statements)
    ).apply(instance, FunctionDeclaration::new));

    @Override
    public StatementType<?> getType() {
        return StatementTypes.FUNCTION_DECLARATION;
    }
}
