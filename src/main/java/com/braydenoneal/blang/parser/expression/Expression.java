package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.builtin.BuiltinExpression;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.operator.BooleanOperator;
import com.braydenoneal.blang.parser.expression.operator.ComparisonOperator;
import com.braydenoneal.blang.parser.expression.operator.UnaryOperator;
import com.braydenoneal.blang.parser.expression.value.*;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.*;

public interface Expression {
    interface Output {
    }

    record Operand(Expression expression) implements Output {
    }

    record Operator(String operator) implements Output {
    }

    Map<String, Integer> operatorPrecedence = Map.ofEntries(
            Map.entry("(", -3),
            Map.entry(")", -3),
            Map.entry("and", -2),
            Map.entry("or", -2),
            Map.entry("==", -1),
            Map.entry("!=", -1),
            Map.entry("<=", -1),
            Map.entry(">=", -1),
            Map.entry("<", -1),
            Map.entry(">", -1),
            Map.entry("+", 0),
            Map.entry("-", 0),
            Map.entry("*", 1),
            Map.entry("/", 1),
            Map.entry("%", 1),
            Map.entry("^", 2)
    );

    Value<?> evaluate();

    static Expression parse(Program program) throws Exception {
        Deque<Output> outputs = new ArrayDeque<>();
        Stack<Operator> operators = new Stack<>();
        boolean openedParenthesis = false;

        while (!(program.peekIs(Type.PARENTHESIS, ")") ||
                program.peekIs(Type.SEMICOLON, ";") ||
                program.peekIs(Type.CURLY_BRACE, "{") ||
                program.peekIs(Type.SQUARE_BRACE, "]") ||
                program.peekIs(Type.COMMA, ",") ||
                program.peekIs(Type.KEYWORD, "else")
        ) || openedParenthesis) {
            switch (program.peek().type()) {
                case Type.BOOLEAN_OPERATOR, Type.COMPARISON_OPERATOR, Type.ARITHMETIC_OPERATOR:
                    String operator = program.next().value();

                    if (!operators.empty() && operatorPrecedence.get(operator) <= operatorPrecedence.get(operators.peek().operator)) {
                        outputs.push(operators.pop());
                    }

                    operators.push(new Operator(operator));
                    break;
                case Type.PARENTHESIS:
                    if (program.next().value().equals("(")) {
                        openedParenthesis = true;
                        operators.push(new Operator("("));
                    } else {
                        openedParenthesis = false;

                        while (!operators.peek().operator.equals("(")) {
                            outputs.push(operators.pop());
                        }

                        operators.pop();
                    }

                    break;
                default:
                    Token token = program.peek();

                    if (program.peekIs(Type.KEYWORD, "fn")) {
                        return FunctionExpression.parse(program);
                    }

                    Expression expression = switch (token.type()) {
                        case Type.BOOLEAN -> new BooleanValue(program.next().value().equals("true"));
                        case Type.NULL -> {
                            program.next();
                            yield Null.value();
                        }
                        case Type.QUOTE -> new StringValue(program.next().value());
                        case Type.FLOAT -> new FloatValue(Float.valueOf(program.next().value()));
                        case Type.INTEGER -> new IntegerValue(Integer.valueOf(program.next().value()));
                        case Type.SQUARE_BRACE -> ListExpression.parse(program);
                        case Type.UNARY_OPERATOR -> {
                            program.next();
                            yield new UnaryOperator(parse(program));
                        }
                        default /* IDENTIFIER */ -> {
                            program.next();

                            if (program.peekIs(Type.PARENTHESIS, "(")) {
                                yield BuiltinExpression.parse(program, token.value());
                            } else if (program.peek().type() == Type.DOT) {
                                yield MemberCallExpression.parse(program, token.value());
                            }

                            yield new VariableExpression(program, token.value());
                        }
                    };

                    List<Expression> indices = new ArrayList<>();

                    while (program.peekIs(Type.SQUARE_BRACE, "[")) {
                        program.next();
                        indices.add(parse(program));
                        program.expect(Type.SQUARE_BRACE, "]");
                    }

                    if (!indices.isEmpty()) {
                        if (expression instanceof VariableExpression variableExpression) {
                            expression = new NamedListAccessExpression(variableExpression.name(), expression, indices);
                        } else if (expression instanceof ListExpression) {
                            expression = new ListAccessExpression(expression, indices);
                        }
                    }

                    if (program.peekIs(Type.KEYWORD, "if")) {
                        expression = IfElseExpression.parse(program, expression);
                    } else if (program.peek().type() == Type.ASSIGN) {
                        expression = AssignmentExpression.parse(program, expression);
                    }

                    outputs.push(new Operand(expression));
                    break;
            }
        }

        while (!operators.empty()) {
            outputs.push(operators.pop());
        }

        Stack<Expression> expressions = new Stack<>();

        while (!outputs.isEmpty()) {
            Output output = outputs.removeLast();

            if (output instanceof Operand(Expression expression)) {
                expressions.add(expression);
            } else if (output instanceof Operator(String operator)) {
                Expression right = expressions.pop();
                Expression left = expressions.pop();

                expressions.add(switch (operator) {
                    case "and", "or" -> new BooleanOperator(operator, left, right);
                    case "==", "!=", "<=", ">=", "<", ">" -> new ComparisonOperator(operator, left, right);
                    default -> new ArithmeticOperator(operator, left, right);
                });
            }
        }

        return expressions.peek();
    }
}
