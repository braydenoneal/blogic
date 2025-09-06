package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.statement.ReturnStatement;
import com.braydenoneal.blang.parser.statement.Statement;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class FunctionValue extends Value<Function> {
    public static final MapCodec<FunctionValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Function.CODEC.fieldOf("value").forGetter(FunctionValue::value)
    ).apply(instance, FunctionValue::new));

    public FunctionValue(Function value) {
        super(value);
    }

    public Value<?> call(Program program, Arguments arguments) {
        return value().call(program, arguments);
    }

    public static Expression parse(Program program) throws ParseException {
        List<String> parameters = new ArrayList<>();
        List<Pair<String, Expression>> defaultParameters = new ArrayList<>();
        boolean parseDefaults = false;

        program.expect(Type.KEYWORD, "fn");

        while (program.peek().type() != Type.COLON) {
            String parameterName = program.expect(Type.IDENTIFIER);

            if (program.peekIs(Type.ASSIGN, "=")) {
                parseDefaults = true;
            }

            if (parseDefaults) {
                try {
                    program.expect(Type.ASSIGN, "=");
                    defaultParameters.add(Pair.of(parameterName, Expression.parse(program)));
                } catch (ParseException e) {
                    throw new ParseException("Function cannot have parameter with default after parameter without default");
                }
            } else {
                parameters.add(parameterName);
            }

            if (program.peek().type() != Type.COLON) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.COLON);
        List<Statement> statements = new ArrayList<>();

        if (program.peekIs(Type.CURLY_BRACE, "{")) {
            program.next();

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program));
            }

            program.expect(Type.CURLY_BRACE, "}");
        } else {
            statements.add(new ReturnStatement(Expression.parse(program)));
        }

        return new FunctionValue(new Function(parameters, defaultParameters, statements));
    }

    @Override
    public ValueType<?> getValueType() {
        return ValueTypes.FUNCTION;
    }

    @Override
    public String toString() {
        return "fn" + value().parameters().toString() + ": " + value().statements().toString();
    }
}
