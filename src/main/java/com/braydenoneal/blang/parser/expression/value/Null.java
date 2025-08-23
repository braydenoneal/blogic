package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Null(boolean none) {
    public static final Codec<Null> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("none").forGetter(Null::none)
    ).apply(instance, Null::new));

    public static NullValue value() {
        return new NullValue(new Null(false));
    }
}
