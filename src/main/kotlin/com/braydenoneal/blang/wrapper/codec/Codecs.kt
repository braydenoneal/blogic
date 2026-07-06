package com.braydenoneal.blang.wrapper.codec

import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec

object Codecs {
    val VARIABLES: Codec<MutableMap<String, Value<*>>> = Codec.unboundedMap(Codec.STRING, Value.CODEC)
}