package blang

import blang.expression.builtin.*
import blang.expression.value.BlockValue
import blang.expression.value.ItemStackValue
import blang.expression.value.ItemValue
import blang.expression.value.TagValue
import parser.Program
import parser.Scope
import parser.expression.Arguments
import parser.expression.Expression
import parser.expression.value.Value
import parser.statement.FunctionDeclaration
import parser.statement.ImportStatement
import parser.statement.StatementList

data class BlogicProgram(
    val context: Context,
    override var source: String = "",
    override var parsed: Boolean = false,
    override var name: String = "",
    override val imports: MutableList<ImportStatement> = mutableListOf(),
    override val statements: StatementList = StatementList(),
    override val functions: MutableMap<String, FunctionDeclaration> = mutableMapOf(),
    override val scopes: MutableList<Scope> = mutableListOf(),
) : Program(source) {
    private var hasRuntimeError = false

    fun runMain() {
        if (hasRuntimeError) {
            return
        }

        try {
            tickMain(functions["main"]!!)
        } catch (e: Exception) {
            log.error("Run main error", e)
            hasRuntimeError = true
        }
    }

    fun tickMain(main: FunctionDeclaration) {
        wait = false

        var result = main.call(this, Arguments.EMPTY)

        while (true) {
            if (result != null) {
                return
            }

            if (wait) {
                break
            }

            result = main.call(this, Arguments.EMPTY)
        }
    }

    override fun parseCustomBuiltins(name: String): Expression? {
        return when (name) {
            "print" -> PrintBuiltin(Arguments.parse(this))
            "block" -> BlockBuiltin(Arguments.parse(this))
            "blockItem" -> BlockItemBuiltin(Arguments.parse(this))
            "breakBlock" -> BreakBlockBuiltin(Arguments.parse(this))
            "deleteItems" -> DeleteItemsBuiltin(Arguments.parse(this))
            "exportAllItems" -> ExportAllItemsBuiltin(Arguments.parse(this))
            "getBlock" -> GetBlockBuiltin(Arguments.parse(this))
            "getItemCount" -> GetItemCountBuiltin(Arguments.parse(this))
            "getItems" -> GetItemsBuiltin(Arguments.parse(this))
            "item" -> ItemBuiltin(Arguments.parse(this))
            "placeBlock" -> PlaceBlockBuiltin(Arguments.parse(this))
            "readItemCount" -> ReadItemCountBuiltin(Arguments.parse(this))
            "tag" -> TagBuiltin(Arguments.parse(this))
            "tags" -> TagsBuiltin(Arguments.parse(this))
            "useItem" -> UseItemBuiltin(Arguments.parse(this))
            else -> null
        }
    }

    override fun getCustomType(value: Value<*>): String? {
        return when (value) {
            is BlockValue -> "block"
            is ItemStackValue -> "itemStack"
            is ItemValue -> "item"
            is TagValue -> "tag"
            else -> null
        }
    }

    override fun getCustomImportProgram(importStatement: ImportStatement): Program {
        var importProgram = this

        for (importName in importStatement.identifiers) {
            for (controller in importProgram.context.entity.getConnectedControllerBlockEntities()) {
                if (controller.program.name == importName) {
                    importProgram = controller.program
                }
            }
        }

        return importProgram
    }
}
