package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.value.*;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public record Arguments(List<Expression> arguments, Map<String, Expression> namedArguments) {
    public Value<?> anyValue(Program program, String name, int index) {
        Expression expression = namedArguments.get(name);

        if (expression == null) {
            if (index >= arguments.size()) {
                throw new RunException("Missing argument " + name);
            }

            expression = arguments.get(index);
        }

        return expression.evaluate(program);
    }

    public BlockValue blockValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof BlockValue value) {
            return value;
        }

        throw new RunException(name + " is not a block");
    }

    public BooleanValue booleanValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof BooleanValue value) {
            return value;
        }

        throw new RunException(name + " is not a boolean");
    }

    public FloatValue floatValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof FloatValue value) {
            return value;
        }

        throw new RunException(name + " is not a float");
    }

    public FunctionValue functionValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof FunctionValue value) {
            return value;
        }

        throw new RunException(name + " is not a function");
    }

    public IntegerValue integerValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof IntegerValue value) {
            return value;
        }

        throw new RunException(name + " is not an integer");
    }

    public ItemStackValue itemStackValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof ItemStackValue value) {
            return value;
        }

        throw new RunException(name + " is not an item stack");
    }

    public ItemValue itemValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof ItemValue value) {
            return value;
        }

        throw new RunException(name + " is not an item");
    }

    public ListValue listValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof ListValue value) {
            return value;
        }

        throw new RunException(name + " is not a list");
    }

    public RangeValue rangeValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof RangeValue value) {
            return value;
        }

        throw new RunException(name + " is not a range");
    }

    public StringValue stringValue(Program program, String name, int index) {
        if (anyValue(program, name, index) instanceof StringValue value) {
            return value;
        }

        throw new RunException(name + " is not a string");
    }

    public static Arguments parse(Program program) throws ParseException {
        List<Expression> arguments = new Stack<>();
        Map<String, Expression> namedArguments = new HashMap<>();
        boolean parseDefaults = false;

        program.expect(Type.PARENTHESIS, "(");

        while (!program.peekIs(Type.PARENTHESIS, ")")) {
            Expression expression = Expression.parse(program);

            if (expression instanceof AssignmentExpression) {
                parseDefaults = true;
            }

            if (parseDefaults) {
                try {
                    AssignmentExpression assignmentExpression = (AssignmentExpression) expression;
                    Expression variableExpression = assignmentExpression.variableExpression();

                    if (variableExpression instanceof VariableExpression(String name) &&
                            assignmentExpression.type().equals("=")
                    ) {
                        namedArguments.put(name, assignmentExpression.expression());
                    } else {
                        throw new ParseException("");
                    }
                } catch (ParseException e) {
                    throw new ParseException("Function cannot have parameter with default after parameter without default");
                }
            } else {
                arguments.add(expression);
            }

            if (!program.peekIs(Type.PARENTHESIS, ")")) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.PARENTHESIS, ")");

        return new Arguments(arguments, namedArguments);
    }

    public static final Arguments EMPTY = new Arguments(List.of(), Map.of());

    public static final Codec<Arguments> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(Arguments::arguments),
            Codec.unboundedMap(Codec.STRING, Expression.CODEC).fieldOf("namedArguments").forGetter(Arguments::namedArguments)
    ).apply(instance, Arguments::new));
}
