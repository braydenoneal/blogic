package com.braydenoneal.blang.testing

import com.braydenoneal.blang.testing.test.*
import java.util.function.Consumer

object Tests {
    private fun tests(): List<Test> {
        return listOf(
            VariableAssignment(),
            NumberLiterals(),
            Strings(),
            Lists(),
            Functions(),
            Operations(),
            MathFunctions(),
            IfStatements(),
            WhileLoops(),
            ForLoops(),
            ControlStatements()
        )
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val results: MutableList<Test.Result> = ArrayList()
        tests().forEach(Consumer { test: Test? -> results.add(test!!.run()) })

        val result = results.stream().reduce(
            Test.Result(0, 0)
        ) { total: Test.Result?, current: Test.Result? ->
            Test.Result(
                total!!.passed + current!!.passed, total.total + current.total
            )
        }


        print("\u001B[31m")

        if (result.passed == result.total) {
            print("\u001B[32m")
        }

        println("\nPassed " + result.passed + " of " + result.total + "\u001B[0m")
    }
}
