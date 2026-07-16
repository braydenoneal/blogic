package blang.expression.builtin

import blang.expression.value.ItemValue
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.value.Value

data class ItemBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        return ItemValue(BuiltInRegistries.ITEM.getValue(Identifier.parse((arguments.stringValue(program, "value", 0) ?: return null).value)))
    }
}
