package blang.expression.builtin

import blang.expression.value.BlockValue
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.value.Value

data class BlockBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        return BlockValue(BuiltInRegistries.BLOCK.getValue(Identifier.parse((arguments.stringValue(program, "value", 0) ?: return null).value)))
    }
}
