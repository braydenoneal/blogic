package com.braydenoneal.blang;

import com.braydenoneal.blang.parser.Program;

public class Blang {
    public static void main(String[] args) throws Exception {
        Program program = new Program("""
                // num = 4;
                // num = num + 12 + 4;
                // print(num);
                // num1 = -12.3;
                // print(num1);
                
                num1 = 12;
                num2 = 0;
                num2 = 4;
                
                fn add(a, b) {
                    c = a + b;
                    return c;
                }
                
                c = add(num1, num2);
                print(c);
                """);

        program.run();
    }
}
