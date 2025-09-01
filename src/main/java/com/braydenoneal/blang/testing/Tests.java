package com.braydenoneal.blang.testing;

import com.braydenoneal.blang.testing.test.*;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    private static List<Test> tests() {
        return List.of(
                new ControlStatements(),
                new VariableAssignment(),
                new NumberLiterals(),
                new Strings(),
                new Lists(),
                new Functions(),
                new Operations(),
                new MathFunctions(),
                new IfStatements(),
                new WhileLoops(),
                new ForLoops()
        );
    }

    public static void main(String[] args) {
        List<Test.Result> results = new ArrayList<>();
        tests().forEach((test) -> results.add(test.run()));

        Test.Result result = results.stream().reduce(new Test.Result(0, 0),
                (Test.Result total, Test.Result current) -> new Test.Result(
                        total.passed() + current.passed(), total.total() + current.total()));


        System.out.print("\u001B[31m");

        if (result.passed() == result.total()) {
            System.out.print("\u001B[32m");
        }

        System.out.println("\nPassed " + result.passed() + " of " + result.total() + "\u001B[0m");
    }
}
