package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;

import java.util.List;

public class NumberLiterals extends Test {
    @Override
    public String body() {
        return """
                a = 1;
                b = -1;
                c = 1.0123;
                d = 1.;
                e = .1;
                f = -1.0123;
                g = -1.;
                h = -.1;
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("a", new IntegerValue(1)),
                new Expect("b", new IntegerValue(-1)),
                new Expect("c", new FloatValue(1.0123f)),
                new Expect("d", new FloatValue(1.f)),
                new Expect("e", new FloatValue(.1f)),
                new Expect("f", new FloatValue(-1.0123f)),
                new Expect("g", new FloatValue(-1.f)),
                new Expect("h", new FloatValue(-.1f))
        );
    }
}
