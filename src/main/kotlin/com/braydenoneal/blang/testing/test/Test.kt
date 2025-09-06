package com.braydenoneal.blang.testing.test

import com.braydenoneal.blang.Context
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.value.Value
import net.minecraft.util.math.BlockPos

abstract class Test {
    private val program = Program("fileName;\n\n" + body(), Context(BlockPos.ORIGIN, null))

    abstract fun body(): String

    abstract fun expects(): List<Expect>

    fun run(): Result {
        val expects = expects()
        var passed = 0
        program.run()

        for (expect in expects) {

            val value = program.topScope().get(expect.name)

            if (expect.value == value) {
                passed++
                println("\u001B[32mPassed: " + expect.name + " is " + expect.value + "\u001B[0m")
            } else {
                println("\u001B[31mFailed: " + expect.name + " is " + value + ", expected " + expect.value + "\u001B[0m")
            }
        }

        print("\u001B[31m")

        if (passed == expects.size) {
            print("\u001B[32m")
        }

        println("Passed " + passed + " of " + expects.size + ": " + this.javaClass.getSimpleName() + "\u001B[0m")
        return Result(passed, expects.size)
    }

    data class Expect(val name: String, val value: Value<*>)

    data class Result(val passed: Int, val total: Int)
}
