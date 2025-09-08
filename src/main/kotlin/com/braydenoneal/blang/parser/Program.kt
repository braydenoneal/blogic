package com.braydenoneal.blang.parser

import com.braydenoneal.blang.Context
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.statement.FunctionDeclaration
import com.braydenoneal.blang.parser.statement.ImportStatement
import com.braydenoneal.blang.parser.statement.Statement
import com.braydenoneal.blang.tokenizer.Token
import com.braydenoneal.blang.tokenizer.Token.Companion.tokenize
import com.braydenoneal.blang.tokenizer.Type
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*

class Program(source: String, private val context: Context) {
    private val imports: MutableList<ImportStatement> = ArrayList()
    private val statements: MutableList<Statement> = ArrayList()
    private val functions: MutableMap<String, FunctionDeclaration> = HashMap()
    private val topScope = Scope(null)
    private val scopes = Stack<Scope>()

    private var hasRuntimeError = false
    private var position = 0

    private val tokens: MutableList<Token>
    private val name: String

    init {
        var tokens: MutableList<Token>

        try {
            tokens = tokenize(source)
        } catch (e: Exception) {
            log.error("Tokenize error", e)
            tokens = ArrayList<Token>()
        }

        this.tokens = tokens
        scopes.push(topScope)
        var name = ""

        try {
            if (!tokens.isEmpty()) {
                name = expect(Type.IDENTIFIER)
                expect(Type.SEMICOLON)
                parse()
            }
        } catch (e: Exception) {
            log.error("Parse error", e)
            statements.clear()
            functions.clear()
        }

        this.name = name
    }

    fun context(): Context {
        return context
    }

    fun run() {
        try {
            for (statement in statements) {
                statement.execute(this)
            }
        } catch (e: Exception) {
            log.error("Run error", e)
        }
    }

    fun runMain() {
        if (hasRuntimeError) {
            return
        }

        try {
            val main = functions["main"]
            main?.call(this, Arguments.EMPTY)
        } catch (e: Exception) {
            log.error("Run main error", e)
            hasRuntimeError = true
        }
    }

    fun name(): String {
        return name
    }

    fun imports(): MutableList<ImportStatement> {
        return imports
    }

    fun addImport(importStatement: ImportStatement) {
        imports.add(importStatement)
    }

    fun parse() {
        while (position < tokens.size) {
            statements.add(Statement.parse(this))
        }
    }

    fun peek(): Token {
        return tokens[position]
    }

    fun peekIs(type: Type, value: String): Boolean {
        val token = peek()
        return token.type == type && token.value == value
    }

    fun peekIs(type: Type): Boolean {
        val token = peek()
        return token.type == type
    }

    fun next(): Token {
        return tokens[position++]
    }

    fun expect(type: Type, value: String) {
        val token = next()

        if (token.type == type && token.value == value) {
            return
        }

        throw ParseException("Expected token of type $type and value $value")
    }

    fun expect(type: Type): String {
        val token = next()

        if (token.type == type) {
            return token.value
        }

        throw ParseException("Expected token of type $type")
    }

    fun addFunction(name: String, function: FunctionDeclaration) {
        functions.put(name, function)
    }

    fun getFunction(name: String): FunctionDeclaration? {
        return functions[name]
    }

    fun newScope() {
        scopes.add(Scope(scopes.peek()))
    }

    fun endScope() {
        scopes.pop()
    }

    fun topScope(): Scope {
        return topScope
    }

    val scope: Scope get() = scopes.peek()

    companion object {
        private val log: Logger = LogManager.getLogger(Program::class.java)
    }
}
