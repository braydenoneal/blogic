package com.braydenoneal.blang.parser

import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec

class Scope(private val parent: Scope?) {
    private var variables: MutableMap<String, Value<*>> = HashMap()

    fun variables(): MutableMap<String, Value<*>> {
        return variables
    }

    fun setVariables(variables: MutableMap<String, Value<*>>) {
        this.variables = variables
    }

    fun get(name: String): Value<*>? {
        val value = variables[name]

        if (value == null && parent != null) {
            return parent.get(name)
        }

        return value
    }

    private fun parentWithVariable(name: String): Scope? {
        if (variables.containsKey(name)) {
            return this
        }

        return parent?.parentWithVariable(name)
    }

    fun set(name: String, value: Value<*>): Value<*> {
        val parentWithVariable = parentWithVariable(name)

        if (parentWithVariable != null) {
            parentWithVariable.variables.put(name, value)
            return value
        }

        variables.put(name, value)
        return value
    }

    fun setLocal(name: String, value: Value<*>) {
        variables.put(name, value)
    }

    fun getLocal(name: String): Value<*>? {
        return variables[name]
    }

    companion object {
        val VARIABLES_CODEC: Codec<MutableMap<String, Value<*>>> = Codec.unboundedMap(Codec.STRING, Value.CODEC)
    }
}
