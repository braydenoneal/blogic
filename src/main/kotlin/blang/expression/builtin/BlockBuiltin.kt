package blang.expression.builtin

import blang.expression.value.BlockValue
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import program.Program
import program.expression.Arguments
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.value.Value

data class BlockBuiltin(override val arguments: Arguments) : Builtin(arguments), Expression {
    override fun evaluate(program: Program): Value<*> {
        return BlockValue(BuiltInRegistries.BLOCK.getValue(Identifier.parse((arguments.stringValue(program, "value", 0)).value)))
    }
}
