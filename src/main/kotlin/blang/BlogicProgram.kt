package blang

import program.Program
import program.RunException
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
    var hasError = false

    fun runMain() {
        val main = functions["main"] ?: return
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
                    break
                }
            }
        }

        return importProgram
    }

    companion object {
        fun cast(program: Program): BlogicProgram {
            return program as? BlogicProgram ?: throw RunException("Program is not a BlogicProgram")
        }
    }
}
