package blang.codec.statement

import blang.codec.Codecs.FUNCT_CODEC
import blang.codec.Codecs.STATEMENT_LIST_CODEC
import blang.codec.expression.ExpressionType
import blang.codec.value.ValueType
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import parser.statement.*

object StatementCodecs {
    val BREAK_STATEMENT_CODEC: MapCodec<BreakStatement> = Codec.unit(BreakStatement()).fieldOf("break_statement")
    val CONTINUE_STATEMENT_CODEC: MapCodec<ContinueStatement> = Codec.unit(ContinueStatement()).fieldOf("continue_statement")
    val DELETE_STATEMENT_CODEC: MapCodec<DeleteStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(DeleteStatement::name),
        ).apply(it, ::DeleteStatement)
    }
    val ELSE_IF_STATEMENT_CODEC: Codec<ElseIfStatement> = RecordCodecBuilder.create {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(ElseIfStatement::condition),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(ElseIfStatement::statements),
            ValueType.CODEC.optionalFieldOf("conditionValue", null).forGetter(ElseIfStatement::conditionValue),
        ).apply(it, ::ElseIfStatement)
    }
    val ELSE_STATEMENT_CODEC: MapCodec<ElseStatement> = mapCodec {
        it.group(
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(ElseStatement::statements),
        ).apply(it, ::ElseStatement)
    }
    val EXPRESSION_STATEMENT_CODEC: MapCodec<ExpressionStatement> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("expression").forGetter(ExpressionStatement::expression),
        ).apply(it, ::ExpressionStatement)
    }
    val FOR_STATEMENT_CODEC: MapCodec<ForStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("itemName").forGetter(ForStatement::itemName),
            ExpressionType.CODEC.fieldOf("listExpression").forGetter(ForStatement::listExpression),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(ForStatement::statements),
        ).apply(it, ::ForStatement)
    }
    val FUNCTION_DECLARATION_CODEC: MapCodec<FunctionDeclaration> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(FunctionDeclaration::name),
            FUNCT_CODEC.fieldOf("function").forGetter(FunctionDeclaration::function),
        ).apply(it, ::FunctionDeclaration)
    }
    val IF_STATEMENT_CODEC: MapCodec<IfStatement> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(IfStatement::condition),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(IfStatement::statements),
            Codec.list(ELSE_IF_STATEMENT_CODEC).fieldOf("elseIfStatements").forGetter(IfStatement::elseIfStatements),
            ELSE_STATEMENT_CODEC.fieldOf("elseStatement").forGetter(IfStatement::elseStatement),
            ValueType.CODEC.optionalFieldOf("conditionValue", null).forGetter(IfStatement::conditionValue),
        ).apply(it, ::IfStatement)
    }
    val IMPORT_STATEMENT_CODEC: MapCodec<ImportStatement> = mapCodec {
        it.group(
            Codec.list(Codec.STRING).fieldOf("identifiers").forGetter(ImportStatement::identifiers),
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
