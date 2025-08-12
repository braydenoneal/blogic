package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.tokenizer.Token;

public class VariableDeclaration implements BlockStatement {
    private Token id;
    private Token type;
    private int listLevel;
    private Expression expression;

    public static VariableDeclaration parse(Program program) {
        /*
        A function in each class that starts at a location in the source code token list and tries to create itself
        ?Returns itself and the next location or it fails?
         */
        return new VariableDeclaration();
    }
}
