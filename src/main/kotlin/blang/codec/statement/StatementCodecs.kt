package blang.codec.statement

import blang.codec.Codecs.FUNCTION_CODEC
import blang.codec.Codecs.FUNCTION_VALUE_CODEC
import blang.codec.Codecs.STATEMENT_LIST_CODEC
import blang.codec.Codecs.STRUCT_DEFINITION_CODEC
import blang.codec.Codecs.mutableListCodec
import blang.codec.Codecs.mutableMapCodec
import blang.codec.expression.ExpressionType
import blang.codec.value.ValueType
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import program.statement.*
import program.statement.IfStatement.ElseIfStatement
import program.statement.IfStatement.ElseStatement
import java.util.*

object StatementCodecs {
    val BREAK_STATEMENT_CODEC: MapCodec<BreakStatement> = MapCodec.unitCodec(BreakStatement()).fieldOf("break_statement")
    val CONTINUE_STATEMENT_CODEC: MapCodec<ContinueStatement> = MapCodec.unitCodec(ContinueStatement()).fieldOf("continue_statement")
    val DELETE_STATEMENT_CODEC: MapCodec<DeleteStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(DeleteStatement::name),
        ).apply(it, ::DeleteStatement)
    }
    val ELSE_IF_STATEMENT_CODEC: Codec<ElseIfStatement> = RecordCodecBuilder.create {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(ElseIfStatement::condition),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(ElseIfStatement::statements),
            ValueType.CODEC.optionalFieldOf("condition_value").forGetter { elseIfStatement -> Optional.ofNullable(elseIfStatement.conditionValue) },
        ).apply(it) { condition, statements, conditionValue -> ElseIfStatement(condition, statements, conditionValue.orElse(null)) }
    }
    val ELSE_STATEMENT_CODEC: MapCodec<ElseStatement> = mapCodec {
        it.group(
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(ElseStatement::statements),
        ).apply(it, ::ElseStatement)
    }
    val EMPTY_STATEMENT_CODEC: MapCodec<EmptyStatement> = MapCodec.unitCodec(EmptyStatement()).fieldOf("empty_statement")
    val EXPRESSION_STATEMENT_CODEC: MapCodec<ExpressionStatement> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("expression").forGetter(ExpressionStatement::expression),
        ).apply(it, ::ExpressionStatement)
    }
    val FOR_STATEMENT_CODEC: MapCodec<ForStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("item_name").forGetter(ForStatement::itemName),
            ExpressionType.CODEC.fieldOf("expression").forGetter(ForStatement::expression),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(ForStatement::statements),
            ValueType.CODEC.optionalFieldOf("value").forGetter { forStatement -> Optional.ofNullable(forStatement.value) },
            Codec.INT.fieldOf("index").forGetter(ForStatement::index),
        ).apply(it) { itemName, expression, statements, value, index ->
            ForStatement(itemName, expression, statements, value.orElse(null), index)
        }
    }
    val FUNCTION_DECLARATION_CODEC: MapCodec<FunctionStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(FunctionStatement::name),
            FUNCTION_CODEC.fieldOf("function").forGetter(FunctionStatement::function),
        ).apply(it, ::FunctionStatement)
    }
    val IF_STATEMENT_CODEC: MapCodec<IfStatement> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(IfStatement::condition),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(IfStatement::statements),
            mutableListCodec(ELSE_IF_STATEMENT_CODEC).fieldOf("else_if_statements").forGetter(IfStatement::elseIfStatements),
            ELSE_STATEMENT_CODEC.codec().optionalFieldOf("else_statement").forGetter { ifStatement -> Optional.ofNullable(ifStatement.elseStatement)},
            ValueType.CODEC.optionalFieldOf("condition_value").forGetter { ifStatement -> Optional.ofNullable(ifStatement.conditionValue) },
        ).apply(it) { condition, statements, elseIfStatements, elseStatement, conditionValue -> IfStatement(condition, statements, elseIfStatements, elseStatement.orElse(null), conditionValue.orElse(null)) }
    }
    val IMPORT_STATEMENT_CODEC: MapCodec<ImportStatement> = mapCodec {
        it.group(
            mutableListCodec(Codec.STRING).fieldOf("identifiers").forGetter(ImportStatement::identifiers),
            Codec.STRING.fieldOf("name").forGetter(ImportStatement::name),
        ).apply(it, ::ImportStatement)
    }
    val RETURN_STATEMENT_CODEC: MapCodec<ReturnStatement> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("expression").forGetter(ReturnStatement::expression),
        ).apply(it, ::ReturnStatement)
    }
    val STATIC_STATEMENT_CODEC: MapCodec<StaticStatement> = mapCodec {
        it.group(
            mutableMapCodec(Codec.STRING, FUNCTION_VALUE_CODEC.codec()).fieldOf("functions").forGetter(StaticStatement::functions),
            mutableMapCodec(Codec.STRING, ExpressionType.CODEC).fieldOf("variables").forGetter(StaticStatement::variables),
        ).apply(it, ::StaticStatement)
    }
    val STATIC_VARIABLE_STATEMENT_CODEC: MapCodec<StaticVariableStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(StaticVariableStatement::name),
            ExpressionType.CODEC.fieldOf("expression").forGetter(StaticVariableStatement::expression),
        ).apply(it, ::StaticVariableStatement)
    }
    val STRUCT_STATEMENT_CODEC: MapCodec<StructStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(StructStatement::name),
            STRUCT_DEFINITION_CODEC.fieldOf("struct").forGetter(StructStatement::struct),
        ).apply(it, ::StructStatement)
    }
    val WHILE_STATEMENT_CODEC: MapCodec<WhileStatement> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(WhileStatement::condition),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(WhileStatement::statements),
        ).apply(it, ::WhileStatement)
    }
}
