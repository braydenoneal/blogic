package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.statement.ReturnStatement;
import com.braydenoneal.blang.parser.statement.Statement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;

public record Function(
        List<String> parameters,
        Map<String, Expression> defaultParameters,
        List<Statement> statements
) {
    public static final Codec<Function> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.STRING).fieldOf("parameters").forGetter(Function::parameters),
            Codec.unboundedMap(Codec.STRING, Expression.CODEC).fieldOf("defaultParameters").forGetter(Function::defaultParameters),
            Codec.list(Statement.CODEC).fieldOf("statements").forGetter(Function::statements)
    ).apply(instance, Function::new));

    public Value<?> call(Program program, Arguments arguments) {
        program.newScope();

        for (int i = 0; i < arguments.arguments().size(); i++) {
            program.getScope().setLocal(parameters.get(i), arguments.arguments().get(i).evaluate(program));
        }

        arguments.namedArguments().forEach((String name, Expression expression) -> program.getScope().setLocal(name, expression.evaluate(program)));

        defaultParameters.forEach((String name, Expression expression) -> {
            if (program.getScope().getLocal(name) == null) {
                program.getScope().setLocal(name, expression.evaluate(program));
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
