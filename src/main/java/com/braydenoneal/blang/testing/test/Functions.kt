package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.IntegerValue;

import java.util.List;

public class Functions extends Test {
    @Override
    public String body() {
        return """
                fn add(a, b) {
                    return a + b;
                }
                
                a = add(12, 4);
                
                nameless = fn a: a + 1;
                
                b = nameless(1);
                
                c = 0;
                
                fn local(c) {
                    return c;
                }
                
                local(1);
                
                fn withDefaults(a, b, c=0) {
                    return c;
                }
                
                d = withDefaults(1, 2);
                e = withDefaults(1, b=2, c=3);
                f = withDefaults(1, 2, 3);
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("a", new IntegerValue(16)),
                new Expect("b", new IntegerValue(2)),
                new Expect("c", new IntegerValue(0)),
                new Expect("d", new IntegerValue(0)),
                new Expect("e", new IntegerValue(3)),
                new Expect("f", new IntegerValue(3))
        );
    }
}
