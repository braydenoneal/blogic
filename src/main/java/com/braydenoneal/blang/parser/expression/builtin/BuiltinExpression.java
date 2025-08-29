package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.CallExpression;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public class BuiltinExpression {
    public static Expression parse(Program program, String name) throws Exception {
        return switch (name) {
            case "abs" -> new AbsoluteValueBuiltin(parseArguments(program).getFirst());
            case "int" -> new IntegerCastBuiltin(parseArguments(program).getFirst());
            case "float" -> new FloatCastBuiltin(parseArguments(program).getFirst());
            case "str" -> new StringCastBuiltin(parseArguments(program).getFirst());
            case "round" -> new RoundBuiltin(parseArguments(program).getFirst());
            case "len" -> new LengthBuiltin(parseArguments(program).getFirst());
            case "block" -> new BlockBuiltin(parseArguments(program).getFirst());
            case "item" -> new ItemBuiltin(parseArguments(program).getFirst());
            case "print" -> new PrintBuiltin(program, parseArguments(program).getFirst());
            case "getBlock" -> new GetBlockBuiltin(program, parseArguments(program));
            case "placeBlock" -> new PlaceBlockBuiltin(program, parseArguments(program));
            case "breakBlock" -> new BreakBlockBuiltin(program, parseArguments(program));
            case "useItem" -> new UseItemBuiltin(program, parseArguments(program));
            case "exportAllItems" -> new ExportAllItemsBuiltin(program, parseArguments(program));
            case "min" -> new MinimumBuiltin(parseArguments(program));
            case "max" -> new MaximumBuiltin(parseArguments(program));
            case "range" -> new RangeBuiltin(parseArguments(program));
            case "getItems" -> new GetItemsBuiltin(program);
            default -> CallExpression.parse(program, name);
        };
    }

    public static List<Expression> parseArguments(Program program) throws Exception {
        List<Expression> arguments = new ArrayList<>();
        program.expect(Type.PARENTHESIS, "(");

        while (!program.peekIs(Type.PARENTHESIS, ")")) {
            arguments.add(Expression.parse(program));

            if (!program.peekIs(Type.PARENTHESIS, ")")) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.PARENTHESIS, ")");

        return arguments;
    }
}
