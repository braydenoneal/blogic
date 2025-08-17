package com.braydenoneal.blang;

import com.braydenoneal.blang.parser.AssignmentStatement;
import com.braydenoneal.blang.parser.FunctionDeclaration;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.ReturnStatement;
import com.braydenoneal.blang.parser.datatype.PrimitiveDataType;
import com.braydenoneal.blang.parser.datatype.PrimitiveDataTypes;
import com.braydenoneal.blang.parser.expression.CallExpression;
import com.braydenoneal.blang.parser.expression.VariableExpression;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;
import java.util.Map;

public class Blang {
    public static void main(String[] args) throws Exception {
        Program program = new Program("");

        program.addFunction(
                "add",
                new FunctionDeclaration(
                        program,
                        new PrimitiveDataType(PrimitiveDataTypes.INTEGER, 0),
                        Map.of("a", new PrimitiveDataType(PrimitiveDataTypes.INTEGER, 0), "b", new PrimitiveDataType(PrimitiveDataTypes.INTEGER, 0)),
                        List.of(
                                new AssignmentStatement(program, "c", null, "=", new ArithmeticOperator("+", new VariableExpression(program, "a"), new VariableExpression(program, "b"))),
                                new ReturnStatement(new VariableExpression(program, "c"))
                        ))
        );

        Value<?> value = new CallExpression(program, "add", List.of(new IntegerValue(12), new IntegerValue(4))).evaluate();
        System.out.println(value.value());
    }
}
