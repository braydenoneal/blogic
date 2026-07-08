package blang.codec

import blang.codec.expression.ExpressionType
import blang.codec.expression.PairCodec.Companion.pair
import blang.codec.statement.StatementCodecs
import blang.codec.statement.StatementType
import blang.codec.value.ValueType
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import parser.Program
import parser.Scope
import parser.expression.value.Funct
import parser.statement.StatementList
import java.util.*

object Codecs {
    fun <T> mutableListCodec(codec: Codec<T>): Codec<MutableList<T>> = Codec.list(codec).xmap(
        { i -> i.toMutableList() },
        { o -> o },
    )

    fun <K, V> mutableMapCodec(keyCodec: Codec<K>, valueCodec: Codec<V>): Codec<MutableMap<K, V>> = Codec.unboundedMap(keyCodec, valueCodec).xmap(
        { i -> i.toMutableMap() },
        { o -> o },
    )

//    fun <T> nullableCodec(codec: Codec<T>): Codec<MutableList<T>> = Codec.list(codec).xmap(
//        { i -> i.toMutableList() },
//        { o -> o },
//    )

    val STATEMENT_LIST_CODEC: MapCodec<StatementList> = mapCodec {
        it.group(
            mutableListCodec(StatementType.CODEC).fieldOf("ran").forGetter(StatementList::ran),
            mutableListCodec(StatementType.CODEC).fieldOf("toRun").forGetter(StatementList::toRun),
        ).apply(it, ::StatementList)
    }
    val FUNCT_CODEC: Codec<Funct> = RecordCodecBuilder.create {
        it.group(
            mutableListCodec(Codec.STRING).fieldOf("parameters").forGetter(Funct::parameters),
            mutableListCodec(pair(Codec.STRING, ExpressionType.CODEC)).fieldOf("defaultParameters").forGetter(Funct::defaultParameters),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(Funct::statements),
            Codec.BOOL.fieldOf("running").forGetter(Funct::running),
        ).apply(it, ::Funct)
    }
    val SCOPE_CODEC: Codec<Scope> = Codec.recursive("scope") { selfCodec ->
        RecordCodecBuilder.create {
            it.group(
                selfCodec.optionalFieldOf("parent").forGetter { scope -> Optional.ofNullable(scope.parent) },
                mutableMapCodec(Codec.STRING, ValueType.CODEC).fieldOf("variables").forGetter(Scope::variables),
            ).apply(it) { parent, variables ->
                Scope(parent.orElse(null), variables)
            }
        }
    }
    val PROGRAM_CODEC: Codec<Program> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("source").forGetter(Program::source),
            Codec.BOOL.fieldOf("parsed").forGetter(Program::parsed),
            Codec.STRING.fieldOf("name").forGetter(Program::name),
            mutableListCodec(StatementCodecs.IMPORT_STATEMENT_CODEC.codec()).fieldOf("imports").forGetter(Program::imports),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(Program::statements),
            mutableMapCodec(Codec.STRING, StatementCodecs.FUNCTION_DECLARATION_CODEC.codec()).fieldOf("functions").forGetter(Program::functions),
            mutableListCodec(SCOPE_CODEC).fieldOf("scopes").forGetter(Program::scopes),
        ).apply(it, ::Program)
    }
}
