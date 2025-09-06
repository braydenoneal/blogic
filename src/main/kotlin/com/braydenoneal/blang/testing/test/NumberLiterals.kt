package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.FloatValue
import com.braydenoneal.blang.parser.expression.value.IntegerValue

class NumberLiterals : Test() {
    override fun body(): String {
        return """
                a = 1;
                b = -1;
                c = 1.0123;
                d = 1.;
                e = .1;
                f = -1.0123;
                g = -1.;
                h = -.1;
                
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("a", IntegerValue(1)),
            Expect("b", IntegerValue(-1)),
            Expect("c", FloatValue(1.0123f)),
            Expect("d", FloatValue(1f)),
            Expect("e", FloatValue(.1f)),
            Expect("f", FloatValue(-1.0123f)),
            Expect("g", FloatValue(-1f)),
            Expect("h", FloatValue(-.1f))
        )
    }
}
