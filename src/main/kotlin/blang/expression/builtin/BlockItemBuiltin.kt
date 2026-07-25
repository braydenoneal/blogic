package blang.expression.builtin

import blang.expression.value.BlockValue
import blang.expression.value.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.value.Value

object BlockItemBuiltin {
    fun call(program: Program, arguments: Arguments): Value<*> {
        return ItemValue(arguments.get<BlockValue>(program, "block").value.asItem())
    }
}
