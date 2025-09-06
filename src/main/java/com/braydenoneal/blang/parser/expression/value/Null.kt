package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.Codec;

public record Null() {
    public static final Codec<Null> CODEC = Codec.unit(new Null());

    public static NullValue value() {
        return new NullValue(new Null());
    }
}
