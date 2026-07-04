package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.ParseException
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator
import com.braydenoneal.blang.parser.expression.operator.BooleanOperator
import com.braydenoneal.blang.parser.expression.operator.ComparisonOperator
import com.braydenoneal.blang.parser.expression.operator.UnaryOperator
import com.braydenoneal.blang.parser.expression.value.*
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import java.util.*

interface Expression {
    fun evaluate(program: Program): Value<*>

    val type: ExpressionType<*>

    interface Output

    data class Operand(val expression: Expression) : Output

    data class Operator(val operator: String) : Output

    companion object {
        val operatorPrecedence: MutableMap<String, Int> = mutableMapOf(
            Pair("(", -3),
            Pair(")", -3),
            Pair("and", -2),
            Pair("or", -2),
            Pair("==", -1),
            Pair("!=", -1),
            Pair("<=", -1),
            Pair(">=", -1),
            Pair("<", -1),
            Pair(">", -1),
            Pair("+", 0),
            Pair("-", 0),
            Pair("*", 1),
            Pair("//", 1),
            Pair("/", 1),
            Pair("%", 1),
            Pair("^", 2),
        )

        fun parse(program: Program): Expression {
            val outputs: Deque<Output> = ArrayDeque()
            val operators: Stack<Operator> = Stack()
            var openedParenthesis = false
            var operand = true

            while (!(program.peekIs(Type.PARENTHESIS, ")") ||
                        program.peekIs(Type.SEMICOLON) ||
                        program.peekIs(Type.CURLY_BRACE) ||
                        program.peekIs(Type.SQUARE_BRACE, "]") ||
                        program.peekIs(Type.COMMA) ||
                        program.peekIs(Type.KEYWORD, "else")) || openedParenthesis || operand
            ) {
                when (program.peek().type) {
                    Type.BOOLEAN_OPERATOR, Type.COMPARISON_OPERATOR, Type.ARITHMETIC_OPERATOR -> {
                        operand = true
                        val operator = program.next().value

                        if (!operators.empty() && operatorPrecedence[operator]!! <= operatorPrecedence[operators.peek().operator]!!) {
                            outputs.push(operators.pop())
                        }

                        operators.push(Operator(operator))
                    }

                    Type.PARENTHESIS -> if (program.next().value == "(") {
                        operand = true
                        openedParenthesis = true
                        operators.push(Operator("("))
                    } else {
                        openedParenthesis = false

                        while (operators.peek().operator != "(") {
                            outputs.push(operators.pop())
                        }

                        operators.pop()
                    }

                    else -> {
                        operand = false
                        val token = program.peek()

                        if (program.peekIs(Type.KEYWORD, "fn")) {
                            return FunctionValue.parse(program)
                        }

                        var expression = when (token.type) {
                            Type.BOOLEAN -> BooleanValue(program.next().value == "true")
                            Type.QUOTE -> StringValue(program.next().value)
                            Type.FLOAT -> FloatValue(program.next().value.toFloat())
                            Type.INTEGER -> IntegerValue(program.next().value.toInt())
                            Type.SQUARE_BRACE -> ListExpression.parse(program)
                            Type.CURLY_BRACE -> StructExpression.parse(program)
                            Type.UNARY_OPERATOR -> UnaryOperator.parse(program)
                            Type.NULL -> Null.parse(program)
                            else -> VariableExpression.parse(program)
                        }

                        val indices: MutableList<Expression> = ArrayList<Expression>()

                        while (program.peekIs(Type.SQUARE_BRACE, "[")) {
                            program.next()
                            indices.add(parse(program))
                            program.expect(Type.SQUARE_BRACE, "]")
                        }

                        if (!indices.isEmpty()) {
                            if (expression is VariableExpression) {
                                expression = NamedListAccessExpression(expression, indices)
                            } else if (expression is ListExpression) {
                                expression = ListAccessExpression(expression, indices)
                            }
                        }

                        while (program.peekIs(Type.DOT)) {
                            program.expect(Type.DOT)
                            val name = program.expect(Type.IDENTIFIER)

                            expression = if (program.peekIs(Type.PARENTHESIS, "(")) {
                                MemberCallExpression.parse(program, expression, name)
                            } else {
                                MemberExpression(expression, name)
                            }
                        }

                        if (program.peekIs(Type.KEYWORD, "if")) {
                            expression = IfElseExpression.parse(program, expression)
                        } else if (program.peekIs(Type.ASSIGN)) {
                            expression = AssignmentExpression.parse(program, expression)
                        }

                        outputs.push(Operand(expression))
                    }
                }
            }

            while (!operators.empty()) {
                outputs.push(operators.pop())
            }

            val expressions = Stack<Expression>()

            while (!outputs.isEmpty()) {
                val output = outputs.removeLast()

                if (output is Operand) {
                    expressions.add(output.expression)
                } else if (output is Operator) {
                    val right = expressions.pop()
                    val left = expressions.pop()
                    val operator = output.operator

                    expressions.add(
                        when (operator) {
                            "and", "or" -> BooleanOperator(operator, left, right)
                            "==", "!=", "<=", ">=", "<", ">" -> ComparisonOperator(operator, left, right)
                            else -> ArithmeticOperator(operator, left, right)
                        }
                    )
                }
            }

            if (expressions.isEmpty()) {
                throw ParseException("Incomplete expression at " + program.peek())
            }

            return expressions.peek()
        }

        val CODEC: Codec<Expression> = ExpressionType.REGISTRY.getCodec().dispatch("type", Expression::type, ExpressionType<*>::codec)
    }
}
