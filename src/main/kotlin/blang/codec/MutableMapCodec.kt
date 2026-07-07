package blang.codec

import com.mojang.serialization.Codec

class MutableMapCodec<A, B> {
    fun convertEncode(values: MutableMap<A, B>): Map<A, B> = values.toMap()

    fun convertDecode(values: Map<A, B>): MutableMap<A, B> = values.toMutableMap()

    fun getCodec(aCodec: Codec<A>, bCodec: Codec<B>): Codec<MutableMap<A, B>> = Codec.unboundedMap(
        aCodec,
        bCodec,
    ).xmap(
        { convertDecode(it) },
        { convertEncode(it) },
    )
}
