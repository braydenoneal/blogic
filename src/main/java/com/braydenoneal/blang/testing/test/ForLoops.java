package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.IntegerValue;

import java.util.List;

public class ForLoops extends Test {
    @Override
    public String body() {
        return """
                a = 0;
                
                for i in [0, 1, 2] {
                    a = i;
                }
                
                b = 0;
                
                for i in range(3) {
                    b = i;
                }
                
                c = 0;
                
                for i in range(0, 3) {
                    c = i;
                }
                
                d = 0;
                
                for i in range(0, 3, 1) {
                    d = i;
                }
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("a", new IntegerValue(2)),
                new Expect("b", new IntegerValue(2)),
                new Expect("c", new IntegerValue(2)),
                new Expect("d", new IntegerValue(2))
        );
    }
}
