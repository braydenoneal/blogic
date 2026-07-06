package blang.expression.builtin

import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Value

data class BlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        return _root_ide_package_.blang.expression.value.BlockValue(Registries.BLOCK.get(Identifier.of((arguments.stringValue(program, "value", 0) ?: return null).value)))
    }
}
