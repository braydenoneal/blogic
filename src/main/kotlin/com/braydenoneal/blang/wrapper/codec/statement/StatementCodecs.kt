package com.braydenoneal.blang.wrapper.codec.statement

import com.braydenoneal.blang.parser.statement.Statement.Companion.STATEMENT_CODEC
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import parser.expression.Expression
import parser.expression.value.Funct
import parser.statement.*

object StatementCodecs {
    val BREAK_STATEMENT_CODEC: MapCodec<BreakStatement> = Codec.unit(BreakStatement()).fieldOf("break_statement")
    val CONTINUE_STATEMENT_CODEC: MapCodec<ContinueStatement> = Codec.unit(ContinueStatement()).fieldOf("continue_statement")
    val DELETE_STATEMENT_CODEC: MapCodec<DeleteStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(DeleteStatement::name)
        ).apply(it, ::DeleteStatement)
    }
    val ELSE_IF_STATEMENT_CODEC: Codec<ElseIfStatement> = RecordCodecBuilder.create {
        it.group(
            Expression.CODEC.fieldOf("condition").forGetter(ElseIfStatement::condition),
            Codec.list(STATEMENT_CODEC).fieldOf("statements").forGetter(ElseIfStatement::statements)
        ).apply(it, ::ElseIfStatement)
    }
    val ELSE_STATEMENT_CODEC: MapCodec<ElseStatement> = mapCodec {
        it.group(
            Codec.list(STATEMENT_CODEC).fieldOf("statements").forGetter(ElseStatement::statements)
        ).apply(it, ::ElseStatement)
    }
    val EXPRESSION_STATEMENT_CODEC: MapCodec<ExpressionStatement> = mapCodec {
        it.group(
            Expression.CODEC.fieldOf("expression").forGetter(ExpressionStatement::expression)
        ).apply(it, ::ExpressionStatement)
    }
    val FOR_STATEMENT_CODEC: MapCodec<ForStatement> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("itemName").forGetter(ForStatement::itemName),
            Expression.CODEC.fieldOf("listExpression").forGetter(ForStatement::listExpression),
            Codec.list(STATEMENT_CODEC).fieldOf("statements").forGetter(ForStatement::statements)
        ).apply(it, ::ForStatement)
    }
    val FUNCTION_DECLARATION_CODEC: MapCodec<FunctionDeclaration> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(FunctionDeclaration::name),
            Funct.CODEC.fieldOf("function").forGetter(FunctionDeclaration::function)
        ).apply(it, ::FunctionDeclaration)
    }
    val IF_STATEMENT_CODEC: MapCodec<IfStatement> = mapCodec {
        it.group(
            Expression.CODEC.fieldOf("condition").forGetter(IfStatement::condition),
            Codec.list(STATEMENT_CODEC).fieldOf("statements").forGetter(IfStatement::statements),
            Codec.list(ELSE_IF_STATEMENT_CODEC).fieldOf("elseIfStatements").forGetter(IfStatement::elseIfStatements),
            ELSE_STATEMENT_CODEC.fieldOf("ElseStatement").forGetter(IfStatement::elseStatement)
        ).apply(it, ::IfStatement)
    }
    val IMPORT_STATEMENT_CODEC: MapCodec<ImportStatement> =
        mapCodec {
            it.group(
                Codec.list(Codec.STRING).fieldOf("identifiers").forGetter(ImportStatement::identifiers)
            ).apply(it, ::ImportStatement)
        }
    val RETURN_STATEMENT_CODEC: MapCodec<ReturnStatement> = mapCodec {
        it.group(
            Expression.CODEC.fieldOf("expression").forGetter(ReturnStatement::expression)
        ).apply(it, ::ReturnStatement)
    }
    val WHILE_STATEMENT_CODEC: MapCodec<WhileStatement> = mapCodec {
        it.group(
            Expression.CODEC.fieldOf("condition").forGetter(WhileStatement::condition),
            Codec.list(STATEMENT_CODEC).fieldOf("statements").forGetter(WhileStatement::statements)
        ).apply(it, ::WhileStatement)
    }
}
