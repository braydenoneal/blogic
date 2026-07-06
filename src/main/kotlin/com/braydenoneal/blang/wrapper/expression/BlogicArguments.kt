package com.braydenoneal.blang.wrapper.expression

import com.braydenoneal.blang.wrapper.expression.value.BlockValue
import com.braydenoneal.blang.wrapper.expression.value.ItemValue
import parser.Program
import parser.RunException
import parser.expression.Arguments

object BlogicArguments {
    fun blockValue(arguments: Arguments, program: Program, name: String, index: Int): BlockValue? {
        val value = arguments.anyValue(program, name, index) ?: return null

        if (value is BlockValue) {
            return value
        }

        throw RunException("$name is not a block")
    }

    fun itemValue(arguments: Arguments, program: Program, name: String, index: Int): ItemValue? {
        val value = arguments.anyValue(program, name, index) ?: return null

        if (value is ItemValue) {
            return value
        }

        throw RunException("$name is not a item")
    }

}
