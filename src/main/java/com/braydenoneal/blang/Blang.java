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
                
                if false {
                    print(1);
                } elif false {
                    print(2);
                } elif false {
                    print(3);
                } else {
                    print(4);
                }
                
                i = 0;
                
                while i < 10 {
                    print(i);
                    i = i + 1;
                }
                """);

        program.run();
    }
}

/*
operator precedence
list literals
better builtin functions logic
type casting, auto casting (2 + 1.2), (int(1.2))
+=, -=
ternary operator
for, iterators
break, continue, pass
++, --
structs
imports, access modifiers
struct implementations
static types
list comprehension?
 */
