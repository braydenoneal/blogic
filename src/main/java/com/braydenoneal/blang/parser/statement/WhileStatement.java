package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public record WhileStatement(Expression condition, List<Statement> statements) implements Statement {
    @Override
    public Statement execute(Program program) {
        long start = System.currentTimeMillis();

        while (condition.evaluate(program) instanceof BooleanValue booleanValue && booleanValue.value()) {
            Statement statement = Statement.runStatements(program, statements);

            if (statement instanceof ReturnStatement) {
                return statement;
            } else if (statement instanceof BreakStatement) {
                break;
            }

            if (System.currentTimeMillis() - start > 15) {
                throw new RunException("Maximum while statement iterations exceeded");
            }
        }

        return this;
    }

    public static Statement parse(Program program) throws ParseException {
        List<Statement> statements = new ArrayList<>();

        program.expect(Type.KEYWORD, "while");
        Expression condition = Expression.parse(program);
        program.expect(Type.CURLY_BRACE, "{");

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");

        return new WhileStatement(condition, statements);
    }

    public static final MapCodec<WhileStatement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("condition").forGetter(WhileStatement::condition),
            Codec.list(Statement.CODEC).fieldOf("statements").forGetter(WhileStatement::statements)
    ).apply(instance, WhileStatement::new));

    @Override
    public StatementType<?> getType() {
        return StatementTypes.WHILE_STATEMENT;
    }
}
