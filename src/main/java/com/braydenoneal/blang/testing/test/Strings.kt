package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.StringValue

class Strings : Test() {
    override fun body(): String {
        return """
                a = "a";
                b = 'b';
                c = a + b;
                d = "1" + 6;
                
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("a", StringValue("a")),
            Expect("b", StringValue("b")),
            Expect("c", StringValue("ab")),
            Expect("d", StringValue("16"))
        )
    }
}
