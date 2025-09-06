package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.FloatValue
import com.braydenoneal.blang.parser.expression.value.IntegerValue

class Operations : Test() {
    override fun body(): String {
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
                
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("num", IntegerValue(3)),
            Expect("bool", BooleanValue(true)),
            Expect("comp", BooleanValue(true)),
            Expect("comp2", BooleanValue(false)),
            Expect("mod1", IntegerValue(15)),
            Expect("mod2", IntegerValue(1)),
            Expect("mod3", IntegerValue(0)),
            Expect("div1", IntegerValue(2)),
            Expect("div2", IntegerValue(2)),
            Expect("div3", FloatValue(2.4f)),
            Expect("div4", FloatValue(2.0f))
        )
    }
}
