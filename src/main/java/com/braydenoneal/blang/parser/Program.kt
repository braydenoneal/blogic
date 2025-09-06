package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.Context;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.statement.FunctionDeclaration;
import com.braydenoneal.blang.parser.statement.ImportStatement;
import com.braydenoneal.blang.parser.statement.Statement;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Program {
    private static final Logger log = LogManager.getLogger(Program.class);
    private final List<ImportStatement> imports = new ArrayList<>();
    private final List<Statement> statements = new ArrayList<>();
    private final Map<String, FunctionDeclaration> functions = new HashMap<>();
    private final Scope topScope = new Scope(null);
    private final Stack<Scope> scopes = new Stack<>();

    private boolean hasRuntimeError = false;
    private int position = 0;

    private final List<Token> tokens;
    private final String name;
    private final Context context;

    public Program(String source, Context context) {
        this.context = context;
        List<Token> tokens;

        try {
            tokens = Token.tokenize(source);
        } catch (Exception e) {
            log.error("Tokenize error", e);
            tokens = new ArrayList<>();
        }

        this.tokens = tokens;
        scopes.push(topScope);
        String name = "";

        try {
            if (!tokens.isEmpty()) {
                name = expect(Type.IDENTIFIER);
                expect(Type.SEMICOLON);
                parse();
            }
        } catch (Exception e) {
            log.error("Parse error", e);
            statements.clear();
            functions.clear();
        }

        this.name = name;
    }

    public Context context() {
        return context;
    }

    public void run() {
        try {
            for (Statement statement : statements) {
                statement.execute(this);
            }
        } catch (Exception e) {
            log.error("Run error", e);
        }
    }

    public void runMain() {
        if (hasRuntimeError) {
            return;
        }

        try {
            FunctionDeclaration main = functions.get("main");

            if (main != null) {
                main.call(this, Arguments.EMPTY);
            }
        } catch (Exception e) {
            log.error("Run main error", e);
            hasRuntimeError = true;
        }
    }

    public String name() {
        return name;
    }

    public List<ImportStatement> imports() {
        return imports;
    }

    public void addImport(ImportStatement importStatement) {
        imports.add(importStatement);
    }

    public void parse() throws ParseException {
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

    public void expect(Type type, String value) throws ParseException {
        Token token = next();

        if (token.type() == type && token.value().equals(value)) {
            return;
        }

        throw new ParseException("Expected token of type " + type + " and value " + value);
    }

    public String expect(Type type) throws ParseException {
        Token token = next();

        if (token.type() == type) {
            return token.value();
        }

        throw new ParseException("Expected token of type " + type);
    }

    public void addFunction(String name, FunctionDeclaration function) {
        functions.put(name, function);
    }

    public FunctionDeclaration getFunction(String name) {
        return functions.get(name);
    }

    public void newScope() {
        scopes.add(new Scope(scopes.peek()));
    }

    public void endScope() {
        scopes.pop();
    }

    public Scope topScope() {
        return topScope;
    }

    public Scope getScope() {
        return scopes.peek();
    }
}
