package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.CallExpression;
import com.braydenoneal.blang.parser.expression.Expression;

public class BuiltinExpression {
    public static Expression parse(Program program, String name) throws ParseException {
        return switch (name) {
            case "abs" -> new AbsoluteValueBuiltin(Arguments.parse(program));
            case "int" -> new IntegerCastBuiltin(Arguments.parse(program));
            case "float" -> new FloatCastBuiltin(Arguments.parse(program));
            case "str" -> new StringCastBuiltin(Arguments.parse(program));
            case "round" -> new RoundBuiltin(Arguments.parse(program));
            case "floor" -> new FloorBuiltin(Arguments.parse(program));
            case "ceil" -> new CeilBuiltin(Arguments.parse(program));
            case "len" -> new LengthBuiltin(Arguments.parse(program));
            case "block" -> new BlockBuiltin(Arguments.parse(program));
            case "item" -> new ItemBuiltin(Arguments.parse(program));
            case "print" -> new PrintBuiltin(Arguments.parse(program));
            case "getBlock" -> new GetBlockBuiltin(Arguments.parse(program));
            case "placeBlock" -> new PlaceBlockBuiltin(Arguments.parse(program));
            case "breakBlock" -> new BreakBlockBuiltin(Arguments.parse(program));
            case "useItem" -> new UseItemBuiltin(Arguments.parse(program));
            case "exportAllItems" -> new ExportAllItemsBuiltin(Arguments.parse(program));
            case "deleteItems" -> new DeleteItemsBuiltin(Arguments.parse(program));
            case "getItems" -> new GetItemsBuiltin(Arguments.parse(program));
            case "min" -> new MinimumBuiltin(Arguments.parse(program));
            case "max" -> new MaximumBuiltin(Arguments.parse(program));
            case "range" -> new RangeBuiltin(Arguments.parse(program));
            default -> new CallExpression(name, Arguments.parse(program));
        };
    }
}
