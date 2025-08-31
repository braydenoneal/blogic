package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.Function;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FunctionDeclaration(
        String name,
        Function function
) implements Statement {
    @Override
    public Statement execute(Program program) {
        return this;
    }

    public Value<?> call(Program program, Arguments arguments) {
        return function().call(program, arguments);
    }

    public static Statement parse(Program program) throws ParseException {
        List<String> parameters = new ArrayList<>();
        Map<String, Expression> defaultParameters = new HashMap<>();
        boolean parseDefaults = false;

        program.expect(Type.KEYWORD, "fn");
        String name = program.expect(Type.IDENTIFIER);
        program.expect(Type.PARENTHESIS, "(");


        while (!program.peekIs(Type.PARENTHESIS, ")")) {
            String parameterName = program.expect(Type.IDENTIFIER);

            if (program.peekIs(Type.ASSIGN, "=")) {
                parseDefaults = true;
            }

            if (parseDefaults) {
                try {
                    program.expect(Type.ASSIGN, "=");
                    defaultParameters.put(parameterName, Expression.parse(program));
                } catch (ParseException e) {
                    throw new ParseException("Function cannot have parameter with default after parameter without default");
                }
            } else {
                parameters.add(parameterName);
            }

            if (!program.peekIs(Type.PARENTHESIS, ")")) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.PARENTHESIS, ")");
        program.expect(Type.CURLY_BRACE, "{");
        List<Statement> statements = new ArrayList<>();

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");

        FunctionDeclaration functionDeclaration = new FunctionDeclaration(name, new Function(parameters, defaultParameters, statements));
        program.addFunction(name, functionDeclaration);
        return functionDeclaration;
    }

    public static final MapCodec<FunctionDeclaration> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(FunctionDeclaration::name),
            Function.CODEC.fieldOf("function").forGetter(FunctionDeclaration::function)
    ).apply(instance, FunctionDeclaration::new));

    @Override
    public StatementType<?> getType() {
        return StatementTypes.FUNCTION_DECLARATION;
    }
}
