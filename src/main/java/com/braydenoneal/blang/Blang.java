package com.braydenoneal.blang;

import com.braydenoneal.blang.parser.Program;

public class Blang {
    public static void main(String[] args) throws Exception {
        Program program1 = new Program("""
                num = 0;
                num = 12;
                print(num);
                num1 = -12.3;
                print(num1);
                """);

        program1.run();
    }
}
