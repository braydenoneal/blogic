package com.braydenoneal.blang.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Token(String value, Type type) {
    public static List<Token> tokenize(String source) throws TokenException {
        List<Token> tokens = new ArrayList<>();
        int position = 0;

        while (position < source.length()) {
            boolean error = true;

            for (Type type : Type.values()) {
                Matcher matcher = Pattern.compile("^" + type.regex).matcher(source.substring(position));

                if (matcher.find()) {
                    String group = type == Type.QUOTE ? matcher.group(0) : matcher.group(1);

                    if (type == Type.QUOTE) {
                        tokens.add(new Token(group.substring(1, group.length() - 1), type));
                    } else if (type != Type.WHITESPACE && type != Type.COMMENT) {
                        tokens.add(new Token(group, type));
                    }

                    position += group.length();
                    error = false;
                    break;
                }
            }

            if (error) {
                throw new TokenException("Unrecognized character '" + source.charAt(position) + "' at position " + position);
            }
        }

        return tokens;
    }
}
