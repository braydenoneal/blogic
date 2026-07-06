package com.braydenoneal.blang.wrapper.expression.builtin

import com.braydenoneal.blang.wrapper.expression.BlogicArguments.itemValue
import com.braydenoneal.blang.wrapper.expression.value.TagValue
import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.ListValue
import parser.expression.value.Value

data class TagsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        val item = (itemValue(arguments, program, "value", 0) ?: return null).value
        val tags = ArrayList<Value<*>>()

        item.defaultStack.streamTags().forEach { tags.add(TagValue(it)) }

        return ListValue(tags)
    }
}
