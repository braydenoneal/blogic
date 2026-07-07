package blang.codec

import blang.codec.expression.ExpressionType
import blang.codec.expression.PairCodec.Companion.pair
import blang.codec.statement.StatementType
import blang.codec.value.ValueType
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import parser.expression.value.Funct
import parser.expression.value.Value
import parser.statement.StatementList

object Codecs {
    val VARIABLES_CODEC: Codec<MutableMap<String, Value<*>>> = Codec.unboundedMap(Codec.STRING, ValueType.CODEC)
    val STATEMENT_LIST_CODEC: MapCodec<StatementList> = mapCodec {
        it.group(
            Codec.list(StatementType.CODEC).fieldOf("ran").forGetter(StatementList::ran),
            Codec.list(StatementType.CODEC).fieldOf("toRun").forGetter(StatementList::toRun),
        ).apply(it, ::StatementList)
    }
    val FUNCT_CODEC: Codec<Funct> = RecordCodecBuilder.create {
        it.group(
            Codec.list(Codec.STRING).fieldOf("parameters").forGetter(Funct::parameters),
            Codec.list(pair(Codec.STRING, ExpressionType.CODEC)).fieldOf("defaultParameters").forGetter(Funct::defaultParameters),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(Funct::statements),
            Codec.BOOL.fieldOf("running").forGetter(Funct::running),
        ).apply(it, ::Funct)
    }
}
