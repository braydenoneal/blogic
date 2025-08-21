package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.Context;
import com.braydenoneal.blang.parser.statement.FunctionDeclaration;
import com.braydenoneal.blang.parser.statement.Statement;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.*;

public class Program {
    private final List<Token> tokens;
    private int position;
    private final List<Statement> statements;
    private final Map<String, FunctionDeclaration> functions;
    private final Stack<Scope> scopes;
    private final Context context;

    public Program(String source, Context context) {
        List<Token> tokens;
        this.context = context;
        try {
            tokens = Token.tokenize(source);
        } catch (Exception e) {
            System.out.println("Tokenize error: " + e);
            tokens = new ArrayList<>();
        }
        this.tokens = tokens;
        position = 0;
        statements = new ArrayList<>();
        functions = new HashMap<>();
        scopes = new Stack<>();
        scopes.push(new Scope(null));
        try {
            parse();
        } catch (Exception e) {
            System.out.println("Parse error: " + e);
        }
    }

    public Context context() {
        return context;
    }

    public void run() {
        try {
            for (Statement statement : statements) {
                statement.execute();
            }
        } catch (Exception e) {
            System.out.println("Run error: " + e);
        }
    }

    public void runMain() {
        try {
            FunctionDeclaration main = functions.get("main");

            if (main != null) {
                main.call();
            }
        } catch (Exception e) {
            System.out.println("Run main error: " + e);
        }
    }

    public void parse() throws Exception {
        while (position < tokens.size()) {
            statements.add(Statement.parse(this));
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

    public void addFunction(String name, FunctionDeclaration function) {
        functions.put(name, function);
    }

    public FunctionDeclaration getFunction(String name) {
        return functions.get(name);
    }

    public Scope getScope() {
        return scopes.peek();
    }
}
