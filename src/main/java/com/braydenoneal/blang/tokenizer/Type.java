package com.braydenoneal.blang.tokenizer;

public enum Type {
    WHITESPACE("[ \\n\\t\\r]"),
    COMMENT("(\\/\\/([^\\n]*)|\\/\\*(.*)\\*\\//s)"),
    KEYWORD("(and|or|pub|struct|impl|var|fn|bool|int|float|str|char|return|continue|pass|if|else|elif|while|for|in|loop)"),
    BOOLEAN("(true|false)"),
    IDENTIFIER("[A-Za-z_][A-Za-z0-9_]*"),
    QUOTE("(\"|')(?:\\\\\\1|(?!\\1).)*(\\1)"),
    NUMBER("(?:0x)?[0-9_]+(?:\\.[0-9_]*)?"),
    CURLY_BRACE("(\\{|\\})"),
    PARENTHESIS("(\\(|\\))"),
    SQUARE_BRACE("(\\[|\\])"),
    COMPARISON_OPERATOR("(==|!=|<=|>=|<|>)"),
    ASSIGN("(=|\\+=|-=)"),
    DOT("\\."),
    COMMA(","),
    SEMICOLON(";"),
    UNARY_OPERATOR("(!|++|--)"),
    BINARY_OPERATOR("(\\+|\\-|\\*|\\/|%|\\^)"),
    TERNARY_OPERATOR("[?:]"),
    ;

    public final String regex;

    Type(String regex) {
        this.regex = regex;
    }
}
