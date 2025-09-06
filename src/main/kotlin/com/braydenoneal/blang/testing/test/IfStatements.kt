package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.IntegerValue

class IfStatements : Test() {
    override fun body(): String {
        return """
                a = 0;
                
                if true {
                    a = 1;
                }
                
                b = 0;
                
                if false {
                    b = 1;
                } else {
                    b  = 2;
                }
                
                c = 0;
                
                if false {
                    c = 1;
                } elif true {
                    c = 3;
                } else {
                    c = 2;
                }
                
                d = 4 if true else 0;
                
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("a", IntegerValue(1)),
            Expect("b", IntegerValue(2)),
            Expect("c", IntegerValue(3)),
            Expect("d", IntegerValue(4))
        )
    }
}
