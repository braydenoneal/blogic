package com.braydenoneal.blang;

import com.braydenoneal.blang.parser.Program;

public class Blang {
    public static void main(String[] args) {
        Program program = new Program("""
                fileName;
                import one.two.three;
                import one.two;
                
                # Comment
                
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
                
                print(len([0, 1, 2, 3, 4, 5, 6, 7]));
                
                print([8, 9][1]);
                list = [8, 9, 10];
                print(list[2]);
                
                print(8 < 2 or 8 > 3);
                
                print(-1 % 16);
                print((-1 + 16) % 16);
                
                print([[[0]]][0][0][0]);
                
                i = 0;
                i += 1;
                print(i);
                l = [];
                l += [1];
                print(l);
                
                for i in [2, 1, 3] {
                    print(i);
                }
                
                for i in range(3) {
                    print(i);
                }
                
                for i in range(0, 3) {
                    print(i);
                }
                
                for i in range(0, 3, 1) {
                    print(i);
                }
                
                a = [];
                a += [1];
                a += [2];
                print(a);
                
                print([1] + [0]);
                
                print(1 if true else 0);
                
                print(range(0) == range(0));
                print(0 == 0);
                
                e = [0];
                e.append(2);
                print(e);
                
                a = [[0, [0, [1]]]];
                print(a);
                a[0][1][1][0] = 2;
                print(a);
                print(a[0][1][1][0]);
                
                a = [0];
                b = [1];
                print(a + b);
                
                a = [];
                print(a.append(2));
                print(a.insert(0, 1));
                print(a.remove(0));
                print(a.contains(2));
                print(a.pop());
                
                for i in range(10) {
                    if i == 5 { continue; }
                    print(i);
                }
                
                for i in range(10) {
                    if i == 5 { break; }
                    print(i);
                }
                
                fn emptyReturn() { return; }
                print(emptyReturn());
                
                n = null;
                print(n);
                
                l = fn v: print("Hello, " + v);
                print(l);
                l("world!");
                
                a = 0;
                fn f(a) {
                    print(a);
                }
                f(1);
                print(a);
                
                fn d(a, b, c=0) {
                    print(a);
                    print(b);
                    print(c);
                }
                
                d(1, b=2);
                
                print(c);
                """,
                new Context(null, null));

        program.run();
    }
}

/*
better error context (source location, etc)
unify some of the logic between custom and builtin functions (parsing and arguments)
add block for import?, access modifiers
address while loop max iterations issue
separate language from the game, allowing it to run on its own
structs
better builtin functions logic
struct implementations
static types
dictionaries?
enums?
list comprehension?
in-language errors, exceptions?
codec for entire program?
balancing: breaking blocks requires tool, and takes time

ideas
- portable controller that executes from player
- wait/sleep function to control tick time
- reader, importer, and exporter blocks instead of available everywhere?
- time/amount/too easy balancing
- maybe action functions (read, import, export) each take 1 tick to execute
- read entities like commands (@n, etc)
- storage and auto-crafting features like ae2
- storage blocks like drawers, trash, etc
- chunk loaders
- mob farm features like apothic spawners and mob grind utils
- redstone blocks like switches and indicators
- display blocks for text, items, pixels?, etc
- inventory management features like sophisticated backpacks
- building features like building gadgets
- wireless imports/exports
- fluids and energy
- xp pickup
- ways to control logic with keybinds (create gamepad thingy)

farms
- stone/cobblestone
- moss/azalea
- dirt/oak
- all trees
- sugar cane
- bamboo
- dripstone
- lava
- piglin barter
- smart furnace array
- basic item flow control
- overflow
- advanced filtering with nbt, tags, etc

read
- redstone
- inventory
- block
- fluid
- world
- network

import
- inventory
- block
- fluid
- item
- energy

export
- redstone
- inventory
- block
- fluid
- item
- energy
- interaction
 */
