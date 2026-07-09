package blang.expression.builtin

import blang.expression.BlogicArguments.itemValue
import blang.expression.value.TagValue
import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.ListValue
import parser.expression.value.Value

data class TagsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        val item = (itemValue(arguments, program, "value", 0) ?: return null).value
        val tags = ArrayList<Value<*>>()

        item.defaultInstance.tags().forEach { tags.add(TagValue(it)) }

        return ListValue(tags)
    }
}
