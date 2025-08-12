package com.braydenoneal.blang.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Token(String value, Type type) {
    public static List<Token> tokenize(String source) throws Exception {
        List<Token> tokens = new ArrayList<>();
        int position = 0;

        while (position < source.length()) {
            boolean error = true;

            for (Type type : Type.values()) {
                Matcher matcher = Pattern.compile("^" + type.regex).matcher(source.substring(position));

                if (matcher.find()) {
                    System.out.println(matcher.group());
                    if (type != Type.WHITESPACE && type != Type.COMMENT) {
                        tokens.add(new Token(matcher.group(), type));
                    }

                    position += matcher.group().length();
                    error = false;
                    break;
                }
            }

            if (error) {
                throw new Exception("Error");
            }
        }

        System.out.println(tokens);
        return tokens;
    }
}
