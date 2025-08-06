package com.braydenoneal.blang;

public class Blang {
    public static void main(String[] args) {
        Token.tokenize("""
                public void main() {
                    int foo = bar <= dog;
                    double = "double";
                    single = 'single';
                    nested = "ne 'st' ed";
                }
                """);
    }
}
