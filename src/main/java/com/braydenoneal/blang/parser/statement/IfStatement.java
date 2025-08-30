package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record IfStatement(
        Expression condition,
        List<Statement> statements,
        List<ElseIfStatement> elseIfStatements,
        @Nullable ElseStatement elseStatement
) implements Statement {
    @Override
    public Statement execute(Program program) {
        Value<?> value = condition.evaluate(program);

        if (value instanceof BooleanValue booleanValue && booleanValue.value()) {
            return Statement.runStatements(program, statements);
        }

        for (ElseIfStatement elseIfStatement : elseIfStatements) {
            Value<?> elseIfValue = elseIfStatement.condition().evaluate(program);

            if (elseIfValue instanceof BooleanValue booleanValue && booleanValue.value()) {
                return Statement.runStatements(program, elseIfStatement.statements);
            }
        }

        if (elseStatement == null) {
            return this;
        }

        return Statement.runStatements(program, elseStatement.statements);
    }

    public static Statement parse(Program program) throws ParseException {
        List<Statement> statements = new ArrayList<>();
        List<ElseIfStatement> elseIfStatements = new ArrayList<>();
        ElseStatement elseStatement = null;

        program.expect(Type.KEYWORD, "if");
        Expression condition = Expression.parse(program);
        program.expect(Type.CURLY_BRACE, "{");

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");

        while (program.peekIs(Type.KEYWORD, "elif")) {
            elseIfStatements.add(ElseIfStatement.parse(program));
        }

        if (program.peekIs(Type.KEYWORD, "else")) {
            elseStatement = ElseStatement.parse(program);
        }

        return new IfStatement(condition, statements, elseIfStatements, elseStatement);
    }

    record ElseIfStatement(Expression condition, List<Statement> statements) {
        public static ElseIfStatement parse(Program program) throws ParseException {
            List<Statement> statements = new ArrayList<>();

            program.expect(Type.KEYWORD, "elif");
            Expression condition = Expression.parse(program);
            program.expect(Type.CURLY_BRACE, "{");

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program));
            }

            program.expect(Type.CURLY_BRACE, "}");

            return new ElseIfStatement(condition, statements);
        }

        public static final Codec<ElseIfStatement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Expression.CODEC.fieldOf("condition").forGetter(ElseIfStatement::condition),
                Codec.list(Statement.CODEC).fieldOf("statements").forGetter(ElseIfStatement::statements)
        ).apply(instance, ElseIfStatement::new));
    }

    record ElseStatement(List<Statement> statements) {
        public static ElseStatement parse(Program program) throws ParseException {
            List<Statement> statements = new ArrayList<>();

            program.expect(Type.KEYWORD, "else");
            program.expect(Type.CURLY_BRACE, "{");

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program));
            }

            program.expect(Type.CURLY_BRACE, "}");

            return new ElseStatement(statements);
        }

        public static final MapCodec<ElseStatement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.list(Statement.CODEC).fieldOf("statements").forGetter(ElseStatement::statements)
        ).apply(instance, ElseStatement::new));
    }

    public static final MapCodec<IfStatement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("condition").forGetter(IfStatement::condition),
            Codec.list(Statement.CODEC).fieldOf("statements").forGetter(IfStatement::statements),
            Codec.list(ElseIfStatement.CODEC).fieldOf("elseIfStatements").forGetter(IfStatement::elseIfStatements),
            ElseStatement.CODEC.fieldOf("ElseStatement").forGetter(IfStatement::elseStatement)
    ).apply(instance, IfStatement::new));

    @Override
    public StatementType<?> getType() {
        return StatementTypes.IF_STATEMENT;
    }
}
