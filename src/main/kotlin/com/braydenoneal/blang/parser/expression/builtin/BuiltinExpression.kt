package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.CallExpression
import com.braydenoneal.blang.parser.expression.Expression

object BuiltinExpression {
    fun parse(program: Program, name: String): Expression {
        return when (name) {
            "abs" -> AbsoluteValueBuiltin(Arguments.parse(program))
            "int" -> IntegerCastBuiltin(Arguments.parse(program))
            "float" -> FloatCastBuiltin(Arguments.parse(program))
            "str" -> StringCastBuiltin(Arguments.parse(program))
            "round" -> RoundBuiltin(Arguments.parse(program))
            "floor" -> FloorBuiltin(Arguments.parse(program))
            "ceil" -> CeilBuiltin(Arguments.parse(program))
            "len" -> LengthBuiltin(Arguments.parse(program))
            "block" -> BlockBuiltin(Arguments.parse(program))
            "item" -> ItemBuiltin(Arguments.parse(program))
            "print" -> PrintBuiltin(Arguments.parse(program))
            "getBlock" -> GetBlockBuiltin(Arguments.parse(program))
            "placeBlock" -> PlaceBlockBuiltin(Arguments.parse(program))
            "breakBlock" -> BreakBlockBuiltin(Arguments.parse(program))
            "useItem" -> UseItemBuiltin(Arguments.parse(program))
            "exportAllItems" -> ExportAllItemsBuiltin(Arguments.parse(program))
            "deleteItems" -> DeleteItemsBuiltin(Arguments.parse(program))
            "getItems" -> GetItemsBuiltin(Arguments.parse(program))
            "getItemCount" -> GetItemCountBuiltin(Arguments.parse(program))
            "min" -> MinimumBuiltin(Arguments.parse(program))
            "max" -> MaximumBuiltin(Arguments.parse(program))
            "range" -> RangeBuiltin(Arguments.parse(program))
            "type" -> TypeBuiltin(Arguments.parse(program))
            else -> CallExpression(name, Arguments.parse(program))
        }
    }
}
