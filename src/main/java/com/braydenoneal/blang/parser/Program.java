package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.statement.*;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.*;

public class Program {
    private final List<Token> tokens;
    private int position;
    private final List<ImportStatement> imports;
    private final List<Statement> statements;
    private final Map<String, FunctionDeclaration> functions;
    private final Stack<Scope> scopes;

    public Program(String source) throws Exception {
        tokens = Token.tokenize(source);
        position = 0;
        imports = new ArrayList<>();
        statements = new ArrayList<>();
        functions = new HashMap<>();
        scopes = new Stack<>();
        scopes.push(new Scope(null));
        parse();
    }

    public void run() {
        for (Statement statement : statements) {
            statement.execute();
        }
    }

    public void parse() throws Exception {
        while (position < tokens.size()) {
            statements.add(parseStatement());
        }
    }

    public Token peek() {
        return tokens.get(position);
    }

    public boolean peekIs(Type type, String value) {
        Token token = peek();
        return token.type() == type && token.value().equals(value);
    }

    public Token next() {
        return tokens.get(position++);
    }

    public void expect(Type type, String value) throws Exception {
        Token token = next();

        if (token.type() == type && token.value().equals(value)) {
            return;
        }

        throw new Exception("Parse error");
    }

    public String expect(Type type) throws Exception {
        Token token = next();

        if (token.type() == type) {
            return token.value();
        }

        throw new Exception("Parse error");
    }

    public Statement parseStatement() throws Exception {
        Token token = peek();

        if (token.type() == Type.KEYWORD) {
            if (token.value().equals("fn")) {
                return FunctionDeclaration.parse(this);
            } else if (token.value().equals("print")) {
                return PrintStatement.parse(this);
            }
        } else if (token.type() == Type.IDENTIFIER) {
            return AssignmentStatement.parse(this);
        }

        return null;
    }

    public void addFunction(String name, FunctionDeclaration function) {
        functions.put(name, function);
    }

    public FunctionDeclaration getFunction(String name) {
        return functions.get(name);
    }

    public void newScope() {
        scopes.push(new Scope(scopes.peek()));
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
