package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;

import java.util.List;

public class Operations extends Test {
    @Override
    public String body() {
        return """
                num = 3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3;
                bool = 8 < 2 or 8 > 3;
                comp = 8 == 8;
                comp2 = 8 != 8;
                mod1 = -1 % 16;
                mod2 = 1 % 16;
                mod3 = 16 % 16;
                div1 = 12 / 5;
                div2 = 12 // 5;
                div3 = 12.0 / 5;
                div4 = 12.0 // 5;
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("num", new IntegerValue(3)),
                new Expect("bool", new BooleanValue(true)),
                new Expect("comp", new BooleanValue(true)),
                new Expect("comp2", new BooleanValue(false)),
                new Expect("mod1", new IntegerValue(15)),
                new Expect("mod2", new IntegerValue(1)),
                new Expect("mod3", new IntegerValue(0)),
                new Expect("div1", new IntegerValue(2)),
                new Expect("div2", new IntegerValue(2)),
                new Expect("div3", new FloatValue(2.4f)),
                new Expect("div4", new FloatValue(2.0f))
        );
    }
}
