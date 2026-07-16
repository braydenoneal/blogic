package blang.codec.statement

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import program.statement.Statement

object StatementTypes {
    val BREAK_STATEMENT = register("break_statement", StatementType(StatementCodecs.BREAK_STATEMENT_CODEC))
    val CONTINUE_STATEMENT = register("continue_statement", StatementType(StatementCodecs.CONTINUE_STATEMENT_CODEC))
    val EMPTY_STATEMENT = register("empty_statement", StatementType(StatementCodecs.EMPTY_STATEMENT_CODEC))
    val EXPRESSION_STATEMENT = register("expression_statement", StatementType(StatementCodecs.EXPRESSION_STATEMENT_CODEC))
    val FOR_STATEMENT = register("for_statement", StatementType(StatementCodecs.FOR_STATEMENT_CODEC))
    val FUNCTION_DECLARATION = register("function_declaration", StatementType(StatementCodecs.FUNCTION_DECLARATION_CODEC))
    val IF_STATEMENT = register("if_statement", StatementType(StatementCodecs.IF_STATEMENT_CODEC))
    val IMPORT_STATEMENT = register("import_statement", StatementType(StatementCodecs.IMPORT_STATEMENT_CODEC))
    val RETURN_STATEMENT = register("return_statement", StatementType(StatementCodecs.RETURN_STATEMENT_CODEC))
    val WHILE_STATEMENT = register("while_statement", StatementType(StatementCodecs.WHILE_STATEMENT_CODEC))
    val DELETE_STATEMENT = register("delete_statement", StatementType(StatementCodecs.DELETE_STATEMENT_CODEC))

    fun <T : Statement> register(id: String, statementType: StatementType<T>): StatementType<T> {
        return Registry.register(
            StatementType.REGISTRY,
            Identifier.fromNamespaceAndPath("blogic", id),
            statementType,
        )
    }

    fun initialize() {
    }
}
