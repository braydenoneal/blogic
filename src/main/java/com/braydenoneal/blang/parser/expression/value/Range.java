package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Range(int start, int end, int step) {
    public static final Codec<Range> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("start").forGetter(Range::start),
            Codec.INT.fieldOf("end").forGetter(Range::end),
            Codec.INT.fieldOf("step").forGetter(Range::step)
    ).apply(instance, Range::new));
}
