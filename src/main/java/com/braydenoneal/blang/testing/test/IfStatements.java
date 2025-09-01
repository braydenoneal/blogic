package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.IntegerValue;

import java.util.List;

public class IfStatements extends Test {
    @Override
    public String body() {
        return """
                a = 0;
                
                if true {
                    a = 1;
                }
                
                b = 0;
                
                if false {
                    b = 1;
                } else {
                    b  = 2;
                }
                
                c = 0;
                
                if false {
                    c = 1;
                } elif true {
                    c = 3;
                } else {
                    c = 2;
                }
                
                d = 4 if true else 0;
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("a", new IntegerValue(1)),
                new Expect("b", new IntegerValue(2)),
                new Expect("c", new IntegerValue(3)),
                new Expect("d", new IntegerValue(4))
        );
    }
}
