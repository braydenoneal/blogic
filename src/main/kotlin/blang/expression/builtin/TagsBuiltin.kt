package blang.expression.builtin

import blang.expression.value.ItemValue
import blang.expression.value.TagValue
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.ListValue
import program.expression.value.Value

data class TagsBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun innerEvaluate(program: Program): Value<*> {
        val item = arguments.get<ItemValue>(program, "value").value
        val tags = ArrayList<Value<*>>()

        item.defaultInstance.tags().forEach { tags.add(TagValue(it)) }

        return ListValue(tags)
    }
}
