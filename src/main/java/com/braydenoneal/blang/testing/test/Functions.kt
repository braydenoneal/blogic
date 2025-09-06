package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.IntegerValue

class Functions : Test() {
    override fun body(): String {
        return """
                fn add(a, b) {
                    return a + b;
                }
                
                a = add(12, 4);
                
                nameless = fn a: a + 1;
                
                b = nameless(1);
                
                c = 0;
                
                fn local(c) {
                    return c;
                }
                
                local(1);
                
                fn withDefaults(a, b, c=0) {
                    return c;
                }
                
                d = withDefaults(1, 2);
                e = withDefaults(1, b=2, c=3);
                f = withDefaults(1, 2, 3);
                
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("a", IntegerValue(16)),
            Expect("b", IntegerValue(2)),
            Expect("c", IntegerValue(0)),
            Expect("d", IntegerValue(0)),
            Expect("e", IntegerValue(3)),
            Expect("f", IntegerValue(3))
        )
    }
}
