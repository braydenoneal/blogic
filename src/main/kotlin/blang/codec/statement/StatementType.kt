package blang.codec.statement

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import parser.statement.*
import java.util.function.Function

data class StatementType<T : Statement>(val codec: MapCodec<T>) {
    companion object {
        val type: Function<in Statement, out StatementType<*>> = { statement: Statement ->
            when (statement) {
                is BreakStatement -> StatementTypes.BREAK_STATEMENT
                is ContinueStatement -> StatementTypes.CONTINUE_STATEMENT
                is DeleteStatement -> StatementTypes.DELETE_STATEMENT
                is ExpressionStatement -> StatementTypes.EXPRESSION_STATEMENT
                is ForStatement -> StatementTypes.FOR_STATEMENT
                is FunctionDeclaration -> StatementTypes.FUNCTION_DECLARATION
                is IfStatement -> StatementTypes.IF_STATEMENT
                is ImportStatement -> StatementTypes.IMPORT_STATEMENT
                is ReturnStatement -> StatementTypes.RETURN_STATEMENT
                is WhileStatement -> StatementTypes.WHILE_STATEMENT
                else -> throw Exception("Statement type not found")
            }
        }

        val REGISTRY: Registry<StatementType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("blogic", "statement_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Statement> = REGISTRY.byNameCodec().dispatch("type", type, StatementType<*>::codec)
    }
}
