package com.braydenoneal.blang.tokenizer;

public enum Type {
    WHITESPACE("[ \\n\\t\\r]"),
    COMMENT("(\\/\\/([^\\n]*)|\\/\\*(.*)\\*\\//s)"),
    KEYWORD("(pub|struct|impl|var|fn|bool|int|float|str|char|return|continue|pass|if|else|elif|while|for|in|loop|print)"),
    BOOLEAN_OPERATOR("(and|or)"),
    BOOLEAN("(true|false)"),
    IDENTIFIER("[A-Za-z_][A-Za-z0-9_]*"),
    QUOTE("(\"|')(?:\\\\\\1|(?!\\1).)*(\\1)"),
    FLOAT("(-?[0-9]+\\.[0-9]*)"),
    INTEGER("(-?[0-9]+)"),
    CURLY_BRACE("(\\{|\\})"),
    PARENTHESIS("(\\(|\\))"),
    SQUARE_BRACE("(\\[|\\])"),
    COMPARISON_OPERATOR("(==|!=|<=|>=|<|>)"),
    ASSIGN("(=|\\+=|-=)"),
    DOT("\\."),
    COMMA(","),
    SEMICOLON(";"),
    UNARY_OPERATOR("(!|\\+\\+|--)"),
    ARITHMETIC_OPERATOR("(\\+|\\-|\\*|\\/|%|\\^)"),
    TERNARY_OPERATOR("[?:]"),
    ;

    public final String regex;

    Type(String regex) {
        this.regex = regex;
    }
}
