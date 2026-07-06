package blang.expression.builtin

import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.ListValue
import parser.expression.value.Value

data class TagsBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        val item = (blang.expression.BlogicArguments.itemValue(arguments, program, "value", 0) ?: return null).value
        val tags = ArrayList<Value<*>>()

        item.defaultStack.streamTags().forEach { tags.add(_root_ide_package_.blang.expression.value.TagValue(it)) }

        return ListValue(tags)
    }
}
