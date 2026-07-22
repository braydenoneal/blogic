package blang.expression.builtin

import blang.expression.value.BlockValue
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import program.Program
import program.expression.Arguments
import program.expression.builtin.Builtin
import program.expression.value.StringValue
import program.expression.value.Value

data class BlockBuiltin(override val arguments: Arguments) : Builtin(arguments) {
    override fun innerEvaluate(program: Program): Value<*> {
        return BlockValue(BuiltInRegistries.BLOCK.getValue(Identifier.parse(arguments.get<StringValue>(program, "value").value)))
    }
}
