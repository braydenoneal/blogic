package blang.expression.builtin

import blang.expression.value.ItemValue
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Value

data class ItemBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        return ItemValue(BuiltInRegistries.ITEM.getValue(Identifier.parse((arguments.stringValue(program, "value", 0) ?: return null).value)))
    }
}
