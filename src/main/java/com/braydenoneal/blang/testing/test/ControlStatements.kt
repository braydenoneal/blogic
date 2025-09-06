package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Null;

import java.util.List;

public class ControlStatements extends Test {
    @Override
    public String body() {
        return """
                list = [];
                
                for i in range(10) {
                    if i == 5 { continue; }
                    list.append(i);
                }
                
                a = list.contains(5);
                
                b = 0;
                
                for i in range(10) {
                    b = i;
                    if i == 5 { break; }
                }
                
                fn emptyReturn() { return; }
                
                c = emptyReturn();
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("a", new BooleanValue(false)),
                new Expect("b", new IntegerValue(5)),
                new Expect("c", Null.value())
        );
    }
}
