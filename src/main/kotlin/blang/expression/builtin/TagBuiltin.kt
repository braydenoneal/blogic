package blang.expression.builtin

import blang.expression.value.TagValue
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import program.Program
import program.expression.Arguments
import program.expression.value.StringValue
import program.expression.value.Value

object TagBuiltin {
    fun call(program: Program, arguments: Arguments): Value<*> {
        return TagValue(TagKey.create(Registries.ITEM, Identifier.parse(arguments.get<StringValue>(program, "value").value)))
    }
}
