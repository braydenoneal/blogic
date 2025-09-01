package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.StringValue;

import java.util.List;

public class MathFunctions extends Test {
    @Override
    public String body() {
        return """
                abs = abs(-1);
                int = int(2.0);
                float = float(3);
                str = str(4);
                round = round(5.4);
                min = min(6, 7);
                max = max(6, 7);
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("abs", new IntegerValue(1)),
                new Expect("int", new IntegerValue(2)),
                new Expect("float", new FloatValue(3.0f)),
                new Expect("str", new StringValue("4")),
                new Expect("round", new IntegerValue(5)),
                new Expect("min", new IntegerValue(6)),
                new Expect("max", new IntegerValue(7))
        );
    }
}
