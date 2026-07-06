package blang.codec.expression

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps

data class PairCodec<A, B>(
    val first: Codec<A>,
    val second: Codec<B>,
) : Codec<Pair<A, B>> {
    override fun <T> encode(value: Pair<A, B>, ops: DynamicOps<T>, rest: T): DataResult<T> {
        return second.encode<T>(value.second, ops, rest).flatMap<T> { f: T -> first.encode<T>(value.first, ops, f) }
    }

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<com.mojang.datafixers.util.Pair<Pair<A, B>, T>> {
        return first.decode<T>(ops, input).flatMap<com.mojang.datafixers.util.Pair<Pair<A, B>, T>> { p1: com.mojang.datafixers.util.Pair<A, T> ->
            second.decode<T>(ops, p1.getSecond()).map<com.mojang.datafixers.util.Pair<Pair<A, B>, T>> { p2: com.mojang.datafixers.util.Pair<B, T> ->
                com.mojang.datafixers.util.Pair.of<Pair<A, B>, T>(Pair<A, B>(p1.getFirst(), p2.getFirst()), p2.getSecond())
            }
        }
    }

    companion object {
        fun <A, B> pair(first: Codec<A>, second: Codec<B>): Codec<Pair<A, B>> {
            return PairCodec(first, second)
        }
    }
}
