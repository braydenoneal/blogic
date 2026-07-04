package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.parser.expression.value.StringValue

class TypeFunction : Test() {
    override fun body(): String {
        return """
                boolean = type(false);
                float = type(0.1);
                function = type(fn a: a);
                integer = type(1);
                list = type([0]);
                nullType = type(null);
                range = type(range(1));
                string = type("string");
                struct = type({a: 1});
                """.trimIndent()
    }

    override fun expects(): List<Expect> {
        return listOf(
            Expect("boolean", StringValue("boolean")),
            Expect("float", StringValue("float")),
            Expect("function", StringValue("function")),
            Expect("integer", StringValue("integer")),
            Expect("list", StringValue("list")),
            Expect("nullType", StringValue("null")),
            Expect("range", StringValue("range")),
            Expect("string", StringValue("string")),
            Expect("struct", StringValue("struct")),
        )
    }
}
