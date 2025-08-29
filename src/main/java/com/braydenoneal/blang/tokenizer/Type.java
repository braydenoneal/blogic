package com.braydenoneal.blang.tokenizer;

public enum Type {
    WHITESPACE("([ \\n\\t\\r])"),
    COMMENT("(\\/\\/([^\\n]*)|\\/\\*(.*)\\*\\//s)"),
    KEYWORD("(import|fn|return|if|else|elif|while|for|in|break|continue)([^A-Za-z0-9_]{1})"),
    BOOLEAN_OPERATOR("(and|or)([^A-Za-z0-9_]{1})"),
    BOOLEAN("(true|false)([^A-Za-z0-9_]{1})"),
    NULL("(null)([^A-Za-z0-9_]{1})"),
    IDENTIFIER("([A-Za-z_][A-Za-z0-9_]*)"),
    QUOTE("(\"|')((?:\\\\\\1|(?!\\1).)*)(\\1)"),
    FLOAT("(-?[0-9]+\\.[0-9]*)"),
    INTEGER("(-?[0-9]+)"),
    CURLY_BRACE("(\\{|\\})"),
    PARENTHESIS("(\\(|\\))"),
    SQUARE_BRACE("(\\[|\\])"),
    COMPARISON_OPERATOR("(==|!=|<=|>=|<|>)"),
    ASSIGN("(=|\\+=|-=)"),
    DOT("(\\.)"),
    COMMA("(,)"),
    SEMICOLON("(;)"),
    COLON("(:)"),
    UNARY_OPERATOR("(!)"),
    ARITHMETIC_OPERATOR("(\\+|\\-|\\*|\\/|%|\\^)"),
    ;

    public final String regex;

    Type(String regex) {
        this.regex = regex;
    }
}
