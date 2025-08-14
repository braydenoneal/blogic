package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.tokenizer.Token;

import java.util.List;

public class Program {
    private List<ImportStatement> imports;
    private List<Statement> statements;
    private final List<Token> tokens;
    private int current;

    public Program(String source) throws Exception {
        tokens = Token.tokenize(source);
        current = 0;
        statements = List.of();
    }

    public void compile() {
        statements = List.of();
    }

    public Token getToken(int offset) {
        return tokens.get(current + offset);
    }

    public void offsetLocation(int offset) {
        current += offset;
    }
}

/*
Program
    [Import Statement], [Block Statement]

Import Statement
    'import', [ID, '.'], ID

Block Statement
    Variable Declaration, ';'
    Call Expression, ';'
    Function Declaration
    Struct Declaration
    Struct Implementation
    If Statement
    While Statement
    For Loop

Expression
    Literal
    ID
    Struct Creation
    Operator
    Member Expression
    Call Expression
    Assign Expression

Struct Creation
    [ID, '.'], ID, '(', [Expression, ','], Expression ')'

Operator
    Unary Operator
    Binary Operator
    Ternary Operator

Member Expression
    ID, '.', ID
    Member Expression, '.', ID
    Call Expression, '.', ID

Call Expression
    ID, '(', [Expression, ','], Expression ')'
    Member Expression, '.', Call Expression

Variable Declaration
    'var', ID, Type, ['[]'], '=', Expression
    'pub', 'var', ID, Type, ['[]'], '=', Expression
    'prot', 'var', ID, Type, ['[]'], '=', Expression

 */
