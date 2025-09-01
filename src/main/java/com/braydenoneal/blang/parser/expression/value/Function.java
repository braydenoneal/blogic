package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.statement.ReturnStatement;
import com.braydenoneal.blang.parser.statement.Statement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Function(
        List<String> parameters,
        List<Pair<String, Expression>> defaultParameters,
        List<Statement> statements
) {
    public static final Codec<Function> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.STRING).fieldOf("parameters").forGetter(Function::parameters),
            Codec.list(Codec.pair(Codec.STRING, Expression.CODEC)).fieldOf("defaultParameters").forGetter(Function::defaultParameters),
            Codec.list(Statement.CODEC).fieldOf("statements").forGetter(Function::statements)
    ).apply(instance, Function::new));

    public Value<?> call(Program program, Arguments arguments) {
        program.newScope();

        arguments.namedArguments().forEach((String name, Expression expression) -> {
            boolean hasDefault = defaultParameters().stream()
                    .anyMatch((var entry) -> entry.getFirst().equals(name));

            if (parameters.contains(name) || hasDefault) {
                program.getScope().setLocal(name, expression.evaluate(program));
            } else {
                throw new RunException("Provided extra argument '" + name + "'");
            }
        });

        for (int i = 0; i < parameters.size(); i++) {
            if (program.getScope().getLocal(parameters.get(i)) == null) {
                if (arguments.arguments().size() > i) {
                    program.getScope().setLocal(parameters.get(i),
                            arguments.arguments().get(i).evaluate(program));
                } else {
                    throw new RunException("Missing argument '" + parameters.get(i) + "'");
                }
            }
        }

        for (int i = parameters.size(); i < arguments.arguments().size(); i++) {
            if (defaultParameters.size() > i - parameters.size()) {
                program.getScope().setLocal(defaultParameters.get(i - parameters.size()).getFirst(),
                        arguments.arguments().get(i).evaluate(program));
            } else {
                throw new RunException("Provided extra argument");
            }
        }

        defaultParameters.forEach((Pair<String, Expression> entry) -> {
            if (program.getScope().getLocal(entry.getFirst()) == null) {
                program.getScope().setLocal(entry.getFirst(), entry.getSecond().evaluate(program));
            }
        });

        Value<?> returnValue = Null.value();
        Statement statement = Statement.runStatements(program, statements);

        if (statement instanceof ReturnStatement returnStatement) {
            returnValue = returnStatement.returnValue(program);
        }

        program.endScope();
        return returnValue;
    }
}
