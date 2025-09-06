package com.braydenoneal.blang.tokenizer

import java.util.regex.Pattern

data class Token(val value: String, val type: Type) {
    companion object {
        fun tokenize(source: String): MutableList<Token> {
            val tokens: MutableList<Token> = ArrayList()
            var position = 0

            while (position < source.length) {
                var error = true

                for (type in Type.entries) {
                    val matcher = Pattern.compile("^" + type.regex).matcher(source.substring(position))

                    if (matcher.find()) {
                        val group = if (type == Type.QUOTE) matcher.group(0) else matcher.group(1)

                        if (type == Type.QUOTE) {
                            tokens.add(Token(group.substring(1, group.length - 1), type))
                        } else if (type != Type.WHITESPACE && type != Type.COMMENT) {
                            tokens.add(Token(group, type))
                        }

                        position += group.length
                        error = false
                        break
                    }
                }

                if (error) {
                    throw TokenException("Unrecognized character '" + source.get(position) + "' at position " + position)
                }
            }

            return tokens
        }
    }
}
