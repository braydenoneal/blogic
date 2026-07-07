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

object Codecs {
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
    val SCOPE_CODEC: Codec<Scope> = Codec.recursive("scope") { selfCodec ->
        RecordCodecBuilder.create {
            it.group(
                selfCodec.optionalFieldOf("parent", null).forGetter(Scope::parent),
                Codec.unboundedMap(Codec.STRING, ValueType.CODEC).fieldOf("variables").forGetter(Scope::variables),
            ).apply(it, ::Scope)
        }
    }
    val PROGRAM_CODEC: Codec<Program> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("source").forGetter(Program::source),
            Codec.BOOL.fieldOf("parsed").forGetter(Program::parsed),
            Codec.STRING.fieldOf("name").forGetter(Program::name),
            Codec.list(StatementCodecs.IMPORT_STATEMENT_CODEC.codec()).fieldOf("imports").forGetter(Program::imports),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(Program::statements),
            Codec.unboundedMap(Codec.STRING, StatementCodecs.FUNCTION_DECLARATION_CODEC.codec()).fieldOf("functions").forGetter(Program::functions),
            Codec.list(SCOPE_CODEC).fieldOf("scopes").forGetter(Program::scopes),
        ).apply(it, ::Program)
    }
}
