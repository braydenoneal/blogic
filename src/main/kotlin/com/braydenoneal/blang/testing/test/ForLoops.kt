package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.IntegerValue

class ForLoops : Test() {
    override fun body(): String {
        return """
                a = 0;
                
                for i in [0, 1, 2] {
                    a = i;
                }
                
                b = 0;
                
                for i in range(3) {
                    b = i;
                }
                
                c = 0;
                
                for i in range(0, 3) {
                    c = i;
                }
                
                d = 0;
                
                for i in range(0, 3, 1) {
                    d = i;
                }
                
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("a", IntegerValue(2)),
            Expect("b", IntegerValue(2)),
            Expect("c", IntegerValue(2)),
            Expect("d", IntegerValue(2))
        )
    }
}
