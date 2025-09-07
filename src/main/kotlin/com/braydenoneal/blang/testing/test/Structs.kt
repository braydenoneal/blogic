package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.StringValue
import com.braydenoneal.blang.parser.expression.value.StructValue
import com.mojang.datafixers.util.Pair

class Structs : Test() {
    override fun body(): String {
        return """
                a = { a: 1 };
                b = a.a;
                c = { a: { a: 1 }};
                d = { a: 1 };
                d.remove("a");
                e = c.keys();
                f = c.values();
                g = c.entries();
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("a", StructValue(mutableListOf(Pair.of("a", IntegerValue(1))))),
            Expect("b", IntegerValue(1)),
            Expect("c", StructValue(mutableListOf(Pair.of("a", StructValue(mutableListOf(Pair.of("a", IntegerValue(1)))))))),
            Expect("d", StructValue(mutableListOf())),
            Expect("e", ListValue(mutableListOf(StringValue("a")))),
            Expect("f", ListValue(mutableListOf(StructValue(mutableListOf(Pair.of("a", IntegerValue(1))))))),
            Expect("g", ListValue(mutableListOf(StructValue(mutableListOf(Pair.of("key", StringValue("a")), Pair.of("value", StructValue(mutableListOf(Pair.of("a", IntegerValue(1)))))))))),
        )
    }
}
