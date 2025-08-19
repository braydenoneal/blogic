package com.braydenoneal.blang;

import com.braydenoneal.blang.parser.Program;

public class Blang {
    public static void main(String[] args) throws Exception {
        Program program = new Program("""
                num = 4;
                num = num + 12 + 4;
                print(num);
                num1 = -12.3;
                print(num1);
                
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
                
                num = 3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3;
                print(num);
                
                list = [3, 4 + 3, [false, 'hello' + ' world']];
                print(list + ["end"]);
                
                print(1.0 + 1);
                
                print("Num: " + 1);
                
                print(abs(-1));
                print(int(2.0));
                print(float(3));
                print(str(4));
                print(round(5.4));
                print(min(6, 7));
                print(max(6, 7));
                """);

        program.run();
    }
}

/*
type casting (int(1.2))
+=, -=
ternary operator
list functions (get, size, etc.)
for, iterators
break, continue, pass
++, --
better tokenizing, parsing, and runtime exceptions
structs
imports, access modifiers
better builtin functions logic
struct implementations
static types
dictionaries?
enums?
list comprehension?
errors, exceptions?
 */
