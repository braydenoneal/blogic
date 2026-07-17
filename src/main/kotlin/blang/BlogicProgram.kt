package blang

import blang.expression.builtin.*
import blang.expression.value.BlockValue
import blang.expression.value.ItemStackValue
import blang.expression.value.ItemValue
import blang.expression.value.TagValue
import parser.Parser
import parser.statement.ArgumentsParser
import program.Program
import program.Scope
import program.expression.Arguments
import program.expression.Expression
import program.expression.value.Value
import program.statement.FunctionStatement
import program.statement.ImportStatement
import program.statement.StatementList

data class BlogicProgram(
    val context: Context,
    override var source: String = "",
    override var parsed: Boolean = false,
    override var name: String = "name",
    override val imports: MutableList<ImportStatement> = mutableListOf(),
    override val statements: StatementList = StatementList(),
    override val functions: MutableMap<String, FunctionStatement> = mutableMapOf(),
    override val scopes: MutableList<Scope> = mutableListOf(),
    var draft: String = source,
    var cursorPosition: Int = 0,
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

    fun tickMain(main: FunctionStatement) {
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

    override fun parseCustomBuiltins(parser: Parser, name: String): Expression? {
        return when (name) {
            "print" -> PrintBuiltin(ArgumentsParser.parse(parser))
            "block" -> BlockBuiltin(ArgumentsParser.parse(parser))
            "blockItem" -> BlockItemBuiltin(ArgumentsParser.parse(parser))
            "breakBlock" -> BreakBlockBuiltin(ArgumentsParser.parse(parser))
            "deleteItems" -> DeleteItemsBuiltin(ArgumentsParser.parse(parser))
            "exportAllItems" -> ExportAllItemsBuiltin(ArgumentsParser.parse(parser))
            "getBlock" -> GetBlockBuiltin(ArgumentsParser.parse(parser))
            "getItemCount" -> GetItemCountBuiltin(ArgumentsParser.parse(parser))
            "getItems" -> GetItemsBuiltin(ArgumentsParser.parse(parser))
            "item" -> ItemBuiltin(ArgumentsParser.parse(parser))
            "placeBlock" -> PlaceBlockBuiltin(ArgumentsParser.parse(parser))
            "readItemCount" -> ReadItemCountBuiltin(ArgumentsParser.parse(parser))
            "tag" -> TagBuiltin(ArgumentsParser.parse(parser))
            "tags" -> TagsBuiltin(ArgumentsParser.parse(parser))
            "useItem" -> UseItemBuiltin(ArgumentsParser.parse(parser))
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
