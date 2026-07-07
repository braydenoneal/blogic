package blang.expression.builtin

import blang.expression.value.TagValue
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Value

data class TagBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        return TagValue(TagKey.of(RegistryKeys.ITEM, Identifier.of((arguments.stringValue(program, "value", 0) ?: return null).value)))
    }
}
