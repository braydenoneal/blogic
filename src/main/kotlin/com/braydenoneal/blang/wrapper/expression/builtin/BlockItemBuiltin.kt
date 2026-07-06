package com.braydenoneal.blang.wrapper.expression.builtin

import com.braydenoneal.blang.wrapper.expression.BlogicArguments.blockValue
import com.braydenoneal.blang.wrapper.expression.value.ItemValue
import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Null
import parser.expression.value.Value

data class BlockItemBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        val item = (blockValue(arguments, program, "block", 0) ?: return null).value.asItem()

        return if (item == null) {
            Null.VALUE
        } else {
            ItemValue(item)
        }
    }
}
