package com.braydenoneal.blang.parser.expression.value

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class Range(val start: Int, val end: Int, val step: Int) {
    companion object {
        val CODEC: Codec<Range> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("start").forGetter(Range::start),
                Codec.INT.fieldOf("end").forGetter(Range::end),
                Codec.INT.fieldOf("step").forGetter(Range::step)
            ).apply(it, ::Range)
        }
    }
}
