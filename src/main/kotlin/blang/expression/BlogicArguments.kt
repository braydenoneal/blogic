package blang.expression

import parser.Program
import parser.RunException
import parser.expression.Arguments

object BlogicArguments {
    fun blockValue(arguments: Arguments, program: Program, name: String, index: Int): blang.expression.value.BlockValue? {
        val value = arguments.anyValue(program, name, index) ?: return null

        if (value is blang.expression.value.BlockValue) {
            return value
        }

        throw RunException("$name is not a block")
    }

    fun itemValue(arguments: Arguments, program: Program, name: String, index: Int): blang.expression.value.ItemValue? {
        val value = arguments.anyValue(program, name, index) ?: return null

        if (value is blang.expression.value.ItemValue) {
            return value
        }

        throw RunException("$name is not a item")
    }
}
