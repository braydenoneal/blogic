package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.value.*;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Stack;

public record Arguments(List<Expression> arguments) {
    public Value<?> anyValue(Program program, String name) {
        if (arguments.isEmpty()) {
            throw new RunException("Missing argument " + name);
        }

        return arguments.removeFirst().evaluate(program);
    }

    public BlockValue blockValue(Program program, String name) {
        if (anyValue(program, name) instanceof BlockValue value) {
            return value;
        }

        throw new RunException(name + " is not a block");
    }

    public BooleanValue booleanValue(Program program, String name) {
        if (anyValue(program, name) instanceof BooleanValue value) {
            return value;
        }

        throw new RunException(name + " is not a boolean");
    }

    public FloatValue floatValue(Program program, String name) {
        if (anyValue(program, name) instanceof FloatValue value) {
            return value;
        }

        throw new RunException(name + " is not a float");
    }

    public FunctionValue functionValue(Program program, String name) {
        if (anyValue(program, name) instanceof FunctionValue value) {
            return value;
        }

        throw new RunException(name + " is not a function");
    }

    public IntegerValue integerValue(Program program, String name) {
        if (anyValue(program, name) instanceof IntegerValue value) {
            return value;
        }

        throw new RunException(name + " is not an integer");
    }

    public ItemStackValue itemStackValue(Program program, String name) {
        if (anyValue(program, name) instanceof ItemStackValue value) {
            return value;
        }

        throw new RunException(name + " is not an item stack");
    }

    public ItemValue itemValue(Program program, String name) {
        if (anyValue(program, name) instanceof ItemValue value) {
            return value;
        }

        throw new RunException(name + " is not an item");
    }

    public ListValue listValue(Program program, String name) {
        if (anyValue(program, name) instanceof ListValue value) {
            return value;
        }

        throw new RunException(name + " is not a list");
    }

    public RangeValue rangeValue(Program program, String name) {
        if (anyValue(program, name) instanceof RangeValue value) {
            return value;
        }

        throw new RunException(name + " is not a range");
    }

    public StringValue stringValue(Program program, String name) {
        if (anyValue(program, name) instanceof StringValue value) {
            return value;
        }

        throw new RunException(name + " is not a string");
    }

    public static Arguments parse(Program program) throws ParseException {
        List<Expression> arguments = new Stack<>();
        program.expect(Type.PARENTHESIS, "(");

        while (!program.peekIs(Type.PARENTHESIS, ")")) {
            arguments.add(Expression.parse(program));

            if (!program.peekIs(Type.PARENTHESIS, ")")) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.PARENTHESIS, ")");

        return new Arguments(arguments);
    }

    public static final Codec<Arguments> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(Arguments::arguments)
    ).apply(instance, Arguments::new));
}
