package com.braydenoneal.blang;

import com.braydenoneal.blang.tokenizer.Token;

public class Blang {
    public static void main(String[] args) throws Exception {
        Token.tokenize("""
                pub main() {
                    var foo int = bar <= dog;
                    var double str = "double";
                    var single str = 'single';
                    var nested str = "ne 's\\"t' ed";
                }
                """);
    }
}
