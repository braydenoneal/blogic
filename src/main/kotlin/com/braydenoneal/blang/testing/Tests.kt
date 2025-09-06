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
//        val ls = ListValue(mutableListOf(IntegerValue(1), ListValue(mutableListOf(IntegerValue(2)))))
//        println(ls.get(mutableListOf(IntegerValue(1), IntegerValue(0))))
//        println(ls)
//        ls.set(mutableListOf(IntegerValue(1), IntegerValue(0)), IntegerValue(3))
////        ls.value[0] = IntegerValue(2)
//        println(ls)
//        return

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
