package com.braydenoneal.blang.wrapper.expression.builtin

import com.braydenoneal.blang.wrapper.expression.value.ItemValue
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import parser.Program
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Value

data class ItemBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*>? {
        return ItemValue(Registries.ITEM.get(Identifier.of((arguments.stringValue(program, "value", 0) ?: return null).value)))
    }
}
