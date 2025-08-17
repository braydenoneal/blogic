package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.scope.Scope;
import com.braydenoneal.blang.tokenizer.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Program {
    private List<ImportStatement> imports;
    private List<Statement> statements;
    private final Map<String, FunctionDeclaration> functions;
    private final Stack<Scope> scopes;
    private final List<Token> tokens;
    private int current;

    public Program(String source) throws Exception {
        tokens = Token.tokenize(source);
        current = 0;
        statements = List.of();
        scopes = new Stack<>();
        functions = new HashMap<>();
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

    public void addFunction(String name, FunctionDeclaration function) {
        functions.put(name, function);
    }

    public FunctionDeclaration getFunction(String name) {
        return functions.get(name);
    }

    public void newScope() {
        scopes.push(new Scope(scopes.isEmpty() ? null : scopes.peek()));
    }

    public Scope getScope() {
        return scopes.peek();
    }

    public void endScope() {
        scopes.pop();
    }
}

/*
Program
    [Import Statement], [Statement]

Import Statement
    'import', [ID, '.'], ID

Statement
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
