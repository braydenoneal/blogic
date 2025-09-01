package com.braydenoneal.blang.testing.test;

import com.braydenoneal.blang.Context;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public abstract class Test {
    private final Program program = new Program("fileName;\n\n" + body(), new Context(null, null));

    public abstract String body();

    public abstract List<Expect> expects();

    public Result run() {
        List<Expect> expects = expects();
        int passed = 0;

        for (Expect expect : expects) {
            program.run();

            Value<?> value = program.topScope().get(expect.name);

            if (expect.value.equals(value)) {
                passed++;
                System.out.println("\u001B[32mPassed: " + expect.name + " is " + expect.value + "\u001B[0m");
            } else {
                System.out.println("\u001B[31mFailed: " + expect.name + " is " + value + ", expected " + expect.value + "\u001B[0m");
            }
        }

        System.out.print("\u001B[31m");

        if (passed == expects.size()) {
            System.out.print("\u001B[32m");
        }

        System.out.println("Passed " + passed + " of " + expects.size() + ": " + this.getClass().getSimpleName() + "\u001B[0m");
        return new Result(passed, expects.size());
    }

    public record Expect(String name, Value<?> value) {
    }

    public record Result(int passed, int total) {
    }
}
