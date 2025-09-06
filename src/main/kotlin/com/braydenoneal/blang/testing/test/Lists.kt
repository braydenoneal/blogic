package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.StringValue

class Lists : Test() {
    override fun body(): String {
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
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("list", ListValue(mutableListOf(BooleanValue(false), IntegerValue(0), StringValue("")))),
            Expect("list2", ListValue(mutableListOf(IntegerValue(0)))),
            Expect("contains", BooleanValue(true)),
            Expect("notContains", BooleanValue(false)),
            Expect("containsAll", BooleanValue(true)),
            Expect("notContainsAll", BooleanValue(false)),
            Expect("list3", ListValue(mutableListOf(IntegerValue(0), IntegerValue(1)))),
            Expect("nested", IntegerValue(1)),
            Expect("nameless", IntegerValue(0)),
            Expect("length", IntegerValue(3))
        )
    }
}
