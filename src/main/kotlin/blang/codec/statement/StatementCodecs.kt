package blang.codec.statement

import blang.codec.Codecs.FUNCTION_CODEC
import blang.codec.Codecs.STATEMENT_LIST_CODEC
import blang.codec.Codecs.mutableListCodec
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
            Codec.STRING.fieldOf("itemName").forGetter(ForStatement::itemName),
            ExpressionType.CODEC.fieldOf("list_expression").forGetter(ForStatement::listExpression),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(ForStatement::statements),
        ).apply(it, ::ForStatement)
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
            ELSE_STATEMENT_CODEC.fieldOf("else_statement").forGetter(IfStatement::elseStatement),
            ValueType.CODEC.optionalFieldOf("condition_value").forGetter { ifStatement -> Optional.ofNullable(ifStatement.conditionValue) },
        ).apply(it) { condition, statements, elseIfStatements, elseStatement, conditionValue -> IfStatement(condition, statements, elseIfStatements, elseStatement, conditionValue.orElse(null)) }
    }
    val IMPORT_STATEMENT_CODEC: MapCodec<ImportStatement> = mapCodec {
        it.group(
            mutableListCodec(Codec.STRING).fieldOf("identifiers").forGetter(ImportStatement::identifiers),
        ).apply(it, ::ImportStatement)
    }
    val RETURN_STATEMENT_CODEC: MapCodec<ReturnStatement> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("expression").forGetter(ReturnStatement::expression),
        ).apply(it, ::ReturnStatement)
    }
    val WHILE_STATEMENT_CODEC: MapCodec<WhileStatement> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(WhileStatement::condition),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(WhileStatement::statements),
        ).apply(it, ::WhileStatement)
    }
}
