package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.statement.ReturnStatement;
import com.braydenoneal.blang.parser.statement.Statement;
import com.braydenoneal.blang.tokenizer.Type;
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

    public Value<?> call(Program program) {
        Value<?> returnValue = null;
        Statement statement = Statement.runStatements(program, value().statements());

        if (statement instanceof ReturnStatement returnStatement) {
            returnValue = returnStatement.returnValue(program);
        }

        return returnValue;
    }

    public static Expression parse(Program program) throws Exception {
        List<String> arguments = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();

        program.expect(Type.KEYWORD, "fn");

        while (program.peek().type() != Type.COLON) {
            arguments.add(program.expect(Type.IDENTIFIER));

            if (program.peek().type() != Type.COLON) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.COLON);

        if (program.peekIs(Type.CURLY_BRACE, "{")) {
            program.next();

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program));
            }

            program.expect(Type.CURLY_BRACE, "}");
        } else {
            statements.add(new ReturnStatement(Expression.parse(program)));
        }

        return new FunctionValue(new Function(arguments, statements));
    }

    @Override
    public ValueType<?> getValueType() {
        return ValueTypes.FUNCTION;
    }

    @Override
    public String toString() {
        return "fn" + value().arguments().toString() + ": " + value().statements().toString();
    }
}
