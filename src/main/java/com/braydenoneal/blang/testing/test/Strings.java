package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.StringValue;

import java.util.List;

public class Strings extends Test {
    @Override
    public String body() {
        return """
                a = "a";
                b = 'b';
                c = a + b;
                d = "1" + 6;
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("a", new StringValue("a")),
                new Expect("b", new StringValue("b")),
                new Expect("c", new StringValue("ab")),
                new Expect("d", new StringValue("16"))
        );
    }
}
