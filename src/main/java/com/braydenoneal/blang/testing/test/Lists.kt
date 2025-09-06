package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.StringValue;

import java.util.List;

public class Lists extends Test {
    @Override
    public String body() {
        return """
                list = [false, 0, ""];
                list2 = [0];
                list2.append(2);
                list2.insert(1, 1);
                list2.pop();
                list2.remove(1);
                contains = list2.contains(0);
                notContains = list2.contains(1);
                containsAll = list.containsAll([false, 0]);
                notContainsAll = list.containsAll([false, 0, 1]);
                list3 = [0];
                list3 += [1];
                listOfList = [0, [0, 0]];
                listOfList[1][1] = 1;
                nested = listOfList[1][1];
                nameless = [[[0]]][0][0][0];
                length = len([0, 1, 2]);
                """;
    }

    @Override
    public List<Expect> expects() {
        return List.of(
                new Expect("list", new ListValue(List.of(new BooleanValue(false), new IntegerValue(0), new StringValue("")))),
                new Expect("list2", new ListValue(List.of(new IntegerValue(0)))),
                new Expect("contains", new BooleanValue(true)),
                new Expect("notContains", new BooleanValue(false)),
                new Expect("containsAll", new BooleanValue(true)),
                new Expect("notContainsAll", new BooleanValue(false)),
                new Expect("list3", new ListValue(List.of(new IntegerValue(0), new IntegerValue(1)))),
                new Expect("nested", new IntegerValue(1)),
                new Expect("nameless", new IntegerValue(0)),
                new Expect("length", new IntegerValue(3))
        );
    }
}
