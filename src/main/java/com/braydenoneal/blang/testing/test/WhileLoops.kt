package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.IntegerValue;

import java.util.List;

public class WhileLoops extends Test {
    @Override
    public String body() {
        return """
                i = 0;
                
                while i < 10 {
                    i += 1;
                }
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("i", new IntegerValue(10))
        );
    }
}
