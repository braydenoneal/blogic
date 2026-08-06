package blang.codec

import blang.codec.expression.ExpressionType
import blang.codec.expression.PairCodec.Companion.pairCodec
import blang.codec.statement.StatementCodecs
import blang.codec.statement.StatementType
import blang.codec.value.ValueType
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import program.Program
import program.Scope
import program.expression.value.function.Function
import program.expression.value.function.FunctionValue
import program.expression.value.struct.StructDefinition
import program.statement.StatementList
import java.util.*

object Codecs {
    fun <T> mutableListCodec(codec: Codec<T>): Codec<MutableList<T>> = Codec.list(codec).xmap(
        { it.toMutableList() },
        { it },
    )

    fun <K, V> mutableMapCodec(keyCodec: Codec<K>, valueCodec: Codec<V>): Codec<MutableMap<K, V>> = Codec.unboundedMap(keyCodec, valueCodec).xmap(
        { it.toMutableMap() },
        { it },
    )

    val STATEMENT_LIST_CODEC: MapCodec<StatementList> = mapCodec {
        it.group(
            mutableListCodec(StatementType.CODEC).fieldOf("statements").forGetter(StatementList::statements),
            Codec.INT.fieldOf("index").forGetter(StatementList::index),
        ).apply(it, ::StatementList)
    }
    val SCOPE_CODEC: Codec<Scope> = Codec.recursive("scope") { selfCodec ->
        RecordCodecBuilder.create {
            it.group(
                selfCodec.optionalFieldOf("parent").forGetter { scope -> Optional.ofNullable(scope.parent) },
                mutableMapCodec(Codec.STRING, ValueType.CODEC).fieldOf("variables").forGetter(Scope::variables),
            ).apply(it) { parent, variables -> Scope(parent.orElse(null), variables) }
        }
    }
    val FUNCTION_CODEC: Codec<Function> = RecordCodecBuilder.create {
        it.group(
            mutableListCodec(Codec.STRING).fieldOf("parameters").forGetter(Function::parameters),
            mutableListCodec(pairCodec(Codec.STRING, ExpressionType.CODEC)).fieldOf("default_parameters").forGetter(Function::defaultParameters),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(Function::statements),
            SCOPE_CODEC.optionalFieldOf("scope").forGetter { function -> Optional.ofNullable(function.scope) },
            Codec.BOOL.fieldOf("running").forGetter(Function::running),
        ).apply(it) { parameters, defaultParameters, statements, scope, running ->
            Function(parameters, defaultParameters, statements, scope.orElse(null), running)
        }
    }
    val FUNCTION_VALUE_CODEC: MapCodec<FunctionValue> = mapCodec {
        it.group(
            FUNCTION_CODEC.fieldOf("value").forGetter(FunctionValue::value),
        ).apply(it, ::FunctionValue)
    }
    val STRUCT_DEFINITION_CODEC: Codec<StructDefinition> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(StructDefinition::name),
            mutableListCodec(Codec.STRING).fieldOf("parameters").forGetter(StructDefinition::parameters),
            mutableListCodec(pairCodec(Codec.STRING, ExpressionType.CODEC)).fieldOf("default_parameters").forGetter(StructDefinition::defaultParameters),
            mutableMapCodec(Codec.STRING, FUNCTION_VALUE_CODEC.codec()).fieldOf("functions").forGetter(StructDefinition::functions),
            mutableMapCodec(Codec.STRING, FUNCTION_VALUE_CODEC.codec()).fieldOf("staticFunctions").forGetter(StructDefinition::staticFunctions),
            mutableMapCodec(Codec.STRING, ExpressionType.CODEC).fieldOf("staticVariables").forGetter(StructDefinition::staticVariables),
        ).apply(it, ::StructDefinition)
    }
    val PROGRAM_CODEC: Codec<Program> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("source").forGetter(Program::source),
            Codec.BOOL.fieldOf("parsed").forGetter(Program::parsed),
            Codec.STRING.fieldOf("name").forGetter(Program::name),
            mutableListCodec(StatementCodecs.IMPORT_STATEMENT_CODEC.codec()).fieldOf("imports").forGetter(Program::imports),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(Program::statements),
            mutableMapCodec(Codec.STRING, FUNCTION_VALUE_CODEC.codec()).fieldOf("functions").forGetter(Program::functions),
            mutableMapCodec(Codec.STRING, STRUCT_DEFINITION_CODEC).fieldOf("structs").forGetter(Program::structs),
            mutableListCodec(SCOPE_CODEC).fieldOf("scopes").forGetter(Program::scopes),
        ).apply(it, ::Program)
    }
}
