package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.StructValue
import com.mojang.datafixers.util.Pair

class Structs : Test() {
    override fun body(): String {
        return """
                a = { a: 1 };
                b = a.a;
                c = { a: { a: 1 }};
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("a", StructValue(listOf(Pair.of("a", IntegerValue(1))))),
            Expect("b", IntegerValue(1)),
            Expect("c", StructValue(listOf(Pair.of("a", StructValue(listOf(Pair.of("a", IntegerValue(1)))))))),
        )
    }
}
