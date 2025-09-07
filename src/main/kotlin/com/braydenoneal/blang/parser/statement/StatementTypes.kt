package com.braydenoneal.blang.parser.statement

import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object StatementTypes {
    val BREAK_STATEMENT = register("break_statement", StatementType(BreakStatement.CODEC))
    val CONTINUE_STATEMENT = register("continue_statement", StatementType(ContinueStatement.CODEC))
    val EXPRESSION_STATEMENT = register("expression_statement", StatementType(ExpressionStatement.CODEC))
    val FOR_STATEMENT = register("for_statement", StatementType(ForStatement.CODEC))
    val FUNCTION_DECLARATION = register("function_declaration", StatementType(FunctionDeclaration.CODEC))
    val IF_STATEMENT = register("if_statement", StatementType(IfStatement.CODEC))
    val IMPORT_STATEMENT = register("import_statement", StatementType(ImportStatement.CODEC))
    val RETURN_STATEMENT = register("return_statement", StatementType(ReturnStatement.CODEC))
    val WHILE_STATEMENT = register("while_statement", StatementType(WhileStatement.CODEC))
    val DELETE_STATEMENT = register("delete_statement", StatementType(DeleteStatement.CODEC))

    fun <T : Statement> register(id: String, statementType: StatementType<T>): StatementType<T> {
        return Registry.register(
            StatementType.REGISTRY,
            Identifier.of("blogic", id),
            statementType
        )
    }

    fun initialize() {
    }
}
