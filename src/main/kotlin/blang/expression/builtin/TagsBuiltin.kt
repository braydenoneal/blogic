package blang.expression.builtin

import blang.expression.BlogicArguments.itemValue
import blang.expression.value.TagValue
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.ListValue
import program.expression.value.Value

data class TagsBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        val item = (itemValue(arguments, program, "value", 0)).value
        val tags = ArrayList<Value<*>>()

        item.defaultInstance.tags().forEach { tags.add(TagValue(it)) }

        return ListValue(tags)
    }
}
