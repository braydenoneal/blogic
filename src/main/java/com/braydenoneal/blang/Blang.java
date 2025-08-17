package com.braydenoneal.blang;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.CallExpression;
import com.braydenoneal.blang.parser.expression.VariableExpression;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.parser.statement.AssignmentStatement;
import com.braydenoneal.blang.parser.statement.FunctionDeclaration;
import com.braydenoneal.blang.parser.statement.ReturnStatement;

import java.util.List;

public class Blang {
    public static void main(String[] args) throws Exception {
        Program program = new Program("");

        program.addFunction(
                "add",
                new FunctionDeclaration(
                        program,
                        List.of("a", "b"),
                        List.of(
                                new AssignmentStatement(
                                        program,
                                        "c",
                                        new ArithmeticOperator("+",
                                                new VariableExpression(program, "a"),
                                                new VariableExpression(program, "b"))
                                ),
                                new ReturnStatement(new VariableExpression(program, "c"))
                        ))
        );

        Value<?> value = new CallExpression(program, "add", List.of(new IntegerValue(12), new IntegerValue(4))).evaluate();
        assert value != null;
        System.out.println(value.value());
    }
}
