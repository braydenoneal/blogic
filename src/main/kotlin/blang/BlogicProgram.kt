package blang

import program.Program
import program.Scope
import program.expression.Arguments
import program.statement.FunctionStatement
import program.statement.ImportStatement
import program.statement.IncompleteException
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
        val main = functions["main"]

        if (main == null || hasRuntimeError) {
            return
        }

        try {
            tickMain(main)
        } catch (e: Exception) {
            log.error("Run main error", e)
            hasRuntimeError = true
        }
    }

    fun tickMain(main: FunctionStatement) {
        wait = false

        while (true) {
            try {
                main.call(this, Arguments.EMPTY)
                return
            } catch (_: IncompleteException) {
                if (wait) {
                    break
                }
            }
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
