package blang.expression.builtin

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Value

data class TagBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        return _root_ide_package_.blang.expression.value.TagValue(TagKey.of(RegistryKeys.ITEM, Identifier.of((arguments.stringValue(program, "value", 0) ?: return null).value)))
    }
}
