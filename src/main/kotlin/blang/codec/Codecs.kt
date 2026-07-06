package blang.codec

import blang.codec.value.ValueType
import com.mojang.serialization.Codec
import parser.expression.value.Value

object Codecs {
    val VARIABLES_CODEC: Codec<MutableMap<String, Value<*>>> = Codec.unboundedMap(Codec.STRING, ValueType.CODEC)
}
