package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.IntegerValue;

import java.util.List;

public class VariableAssignment extends Test {
    @Override
    public String body() {
        return """
                a = 1;
                a = 0;
                b = 0;
                b = a + 1;
                c = b;
                d = 0;
                d += 1;
                d -= 2;
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("a", new IntegerValue(0)),
                new Expect("b", new IntegerValue(1)),
                new Expect("c", new IntegerValue(1)),
                new Expect("d", new IntegerValue(-1))
        );
    }
}
