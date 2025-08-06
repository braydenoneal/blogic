package com.braydenoneal.blang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface Token {
    List<Character> WHITESPACE = List.of(' ', '\n', '\t', '\r');

    Map<String, Type> TWO_CHARACTER_TYPES = Map.ofEntries(
            Map.entry("==", Type.EQUALS),
            Map.entry("<=", Type.LESS_THAN_OR_EQUAL_TO),
            Map.entry(">=", Type.GREATER_THAN_OR_EQUAL_TO),
            Map.entry("!=", Type.NOT_EQUALS),
            Map.entry("+=", Type.PLUS_EQUALS),
            Map.entry("-=", Type.MINUS_EQUALS)
    );

    Map<Character, Type> ONE_CHARACTER_TYPES = Map.ofEntries(
            Map.entry('{', Type.OPEN_PARENTHESIS),
            Map.entry('}', Type.CLOSE_PARENTHESIS),
            Map.entry('(', Type.OPEN_CURLY_BRACE),
            Map.entry(')', Type.CLOSE_CURLY_BRACE),
            Map.entry('[', Type.OPEN_SQUARE_BRACE),
            Map.entry(']', Type.CLOSE_SQUARE_BRACE),
            Map.entry('+', Type.ADD),
            Map.entry('-', Type.SUBTRACT),
            Map.entry('*', Type.MULTIPLY),
            Map.entry('/', Type.DIVIDE),
            Map.entry('%', Type.MODULUS),
            Map.entry('^', Type.POWER),
            Map.entry('=', Type.ASSIGN),
            Map.entry('!', Type.NOT),
            Map.entry('<', Type.LESS_THAN),
            Map.entry('>', Type.GREATER_THAN),
            Map.entry('.', Type.DOT),
            Map.entry(',', Type.COMMA),
            Map.entry(':', Type.COLON),
            Map.entry(';', Type.SEMI_COLON),
            Map.entry('?', Type.QUESTION_MARK)
    );

    Map<String, Type> KEYWORDS = Map.ofEntries(
            Map.entry("and", Type.AND),
            Map.entry("or", Type.OR),
            Map.entry("public", Type.PUBLIC),
            Map.entry("static", Type.STATIC),
            Map.entry("void", Type.VOID),
            Map.entry("bool", Type.BOOL),
            Map.entry("int", Type.INT),
            Map.entry("float", Type.FLOAT),
            Map.entry("str", Type.STR),
            Map.entry("char", Type.CHAR),
            Map.entry("null", Type.NULL),
            Map.entry("return", Type.RETURN),
            Map.entry("continue", Type.CONTINUE),
            Map.entry("pass", Type.PASS),
            Map.entry("if", Type.IF),
            Map.entry("else", Type.ELSE),
            Map.entry("elif", Type.ELIF),
            Map.entry("while", Type.WHILE),
            Map.entry("for", Type.FOR),
            Map.entry("loop", Type.LOOP)
    );

    static void tokenize(String string) {
        List<Token> tokens = new ArrayList<>();
        List<Character> characters = new ArrayList<>(string.codePoints().mapToObj(i -> (char) i).toList());

        while (!characters.isEmpty()) {
            // Whitespace
            if (WHITESPACE.contains(characters.getFirst())) {
                characters.removeFirst();
                continue;
            }

            // TODO: Comments

            // Two characters
            if (characters.size() > 1) {
                String twoCharacter = characters.getFirst() + characters.get(1).toString();

                Type type = TWO_CHARACTER_TYPES.get(twoCharacter);

                if (type != null) {
                    tokens.add(new NonIdentifierToken(type));
                    characters.removeFirst();
                    characters.removeFirst();
                    continue;
                }
            }

            // One character
            Type type = ONE_CHARACTER_TYPES.get(characters.getFirst());

            if (type != null) {
                tokens.add(new NonIdentifierToken(type));
                characters.removeFirst();
                continue;
            }

            // Quotes
            Character quoteCharacter = characters.getFirst();

            if (quoteCharacter == '"' || quoteCharacter == '\'') {
                characters.removeFirst();
                StringBuilder quote = new StringBuilder();

                while (!characters.isEmpty() && characters.getFirst() != quoteCharacter) {
                    quote.append(characters.getFirst());
                    characters.removeFirst();
                }

                if (!characters.isEmpty()) {
                    tokens.add(new QuoteToken(quote.toString(), Type.QUOTE));
                    characters.removeFirst();
                    continue;
                } else {
                    throw new Error("Missing closing quote");
                }
            }

            // Words
            if (Character.isLetter(characters.getFirst())) {
                StringBuilder value = new StringBuilder();

                while (!characters.isEmpty() && Character.isLetter(characters.getFirst())) {
                    value.append(characters.getFirst());
                    characters.removeFirst();
                }

                type = KEYWORDS.get(value.toString());
                tokens.add(new IdentifierToken(value.toString(), Objects.requireNonNullElse(type, Type.IDENTIFIER)));
                characters.removeFirst();
                continue;
            }

            throw new Error("Unrecognized token");
        }

        tokens.add(new NonIdentifierToken(Type.EOF));

        System.out.println(tokens);
    }
}
