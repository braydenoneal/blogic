package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.operator.BooleanOperator;
import com.braydenoneal.blang.parser.expression.operator.ComparisonOperator;
import com.braydenoneal.blang.parser.expression.operator.UnaryOperator;
import com.braydenoneal.blang.parser.expression.value.*;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Stack;

public interface Expression {
    interface Output {
    }

    record Operand(Expression expression) implements Output {
    }

    record Operator(String operator) implements Output {
    }

    Map<String, Integer> operatorPrecedence = Map.ofEntries(
            Map.entry("(", -1),
            Map.entry(")", -1),
            Map.entry("and", 0),
            Map.entry("or", 0),
            Map.entry("==", 0),
            Map.entry("!=", 0),
            Map.entry("<=", 0),
            Map.entry(">=", 0),
            Map.entry("<", 0),
            Map.entry(">", 0),
            Map.entry("+", 0),
            Map.entry("-", 0),
            Map.entry("*", 1),
            Map.entry("/", 1),
            Map.entry("%", 0),
            Map.entry("^", 2)
    );

    Value<?> evaluate();

    static Expression parse(Program program) throws Exception {
        Deque<Output> outputs = new ArrayDeque<>();
        Stack<Operator> operators = new Stack<>();
        boolean openedParenthesis = false;

        while (!(program.peekIs(Type.PARENTHESIS, ")") || program.peekIs(Type.SEMICOLON, ";")) || openedParenthesis) {
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
                    Token token = program.next();

                    Expression expression = switch (token.type()) {
                        case Type.BOOLEAN -> new BooleanValue(token.value().equals("true"));
                        case Type.QUOTE -> new StringValue(token.value());
                        case Type.FLOAT -> new FloatValue(Float.valueOf(token.value()));
                        case Type.INTEGER -> new IntegerValue(Integer.valueOf(token.value()));
                        case Type.UNARY_OPERATOR -> new UnaryOperator(parse(program));
                        default /* IDENTIFIER */ -> {
                            if (program.peekIs(Type.PARENTHESIS, "(")) {
                                yield CallExpression.parse(program, token.value());
                            } else {
                                yield new VariableExpression(program, token.value());
                            }
                        }
                    };

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
        /*
        outs = []
        ops = []
        open_par = false

        while !open_par && peek != ')':
            if peek == op:
                if peek.precedence >= ops.peek.precedence:
                    ops.add(next)
                 else:
                    outs.add(ops.pop)
                    ops.add(next)
            elif peek == '(':
                open_par = true
                ops.add('(')
            elif peek == ')':
                open_par = false
                while ops.peek != '(':
                    outs.add(ops.pop)
                ops.pop
            else:
                outs.add(next)

         while !ops.empty:
            outs.add(ops.pop)

         exps = []

         while !outs.empty:
            if outs.peek is operand:
                exps.add(outs.pop)
            else:
                right = exps.pop
                left = exps.pop
                op = outs.pop
                exps.add(Op{op, left, right})

         return exps.peek
         */
    }
}
