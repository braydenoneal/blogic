package blang.codec.statement

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import program.statement.Statement
import java.util.function.Function
import kotlin.reflect.KClass

data class StatementType<T : Statement>(val codec: MapCodec<T>) {
    companion object {
        val types: MutableMap<KClass<*>, StatementType<*>> = mutableMapOf()

        val type: Function<in Statement, out StatementType<*>> = { statement: Statement ->
            types[statement::class] ?: throw Exception("Statement type not found")
        }

        val REGISTRY: Registry<StatementType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("blogic", "statement_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Statement> = REGISTRY.byNameCodec().dispatch("type", type, StatementType<*>::codec)

        inline fun <reified T : Statement> register(id: String, codec: MapCodec<T>) {
            val type = StatementType(codec)
            types[T::class] = type
            Registry.register(REGISTRY, Identifier.fromNamespaceAndPath("blogic", id), type)
        }

        fun initialize() {
            register("break_statement", StatementCodecs.BREAK_STATEMENT_CODEC)
            register("continue_statement", StatementCodecs.CONTINUE_STATEMENT_CODEC)
            register("empty_statement", StatementCodecs.EMPTY_STATEMENT_CODEC)
            register("expression_statement", StatementCodecs.EXPRESSION_STATEMENT_CODEC)
            register("for_statement", StatementCodecs.FOR_STATEMENT_CODEC)
            register("function_declaration", StatementCodecs.FUNCTION_DECLARATION_CODEC)
            register("if_statement", StatementCodecs.IF_STATEMENT_CODEC)
            register("import_statement", StatementCodecs.IMPORT_STATEMENT_CODEC)
            register("return_statement", StatementCodecs.RETURN_STATEMENT_CODEC)
            register("while_statement", StatementCodecs.WHILE_STATEMENT_CODEC)
            register("delete_statement", StatementCodecs.DELETE_STATEMENT_CODEC)
        }
    }
}
