package blang.expression.builtin

import blang.expression.value.ItemValue
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.StringValue
import program.expression.value.Value

data class ItemBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun innerEvaluate(program: Program): Value<*> {
        return ItemValue(BuiltInRegistries.ITEM.getValue(Identifier.parse(arguments.get<StringValue>(program, "value").value)))
    }
}
