package blang.expression.builtin

import blang.expression.value.ItemValue
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import program.Program
import program.expression.Arguments
import program.expression.value.StringValue
import program.expression.value.Value

object ItemBuiltin {
    fun call(program: Program, arguments: Arguments): Value<*> {
        return ItemValue(BuiltInRegistries.ITEM.getValue(Identifier.parse(arguments.get<StringValue>(program, "value").value)))
    }
}
