package com.braydenoneal.blang.parser.statement;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class StatementTypes {
    public static final StatementType<BreakStatement> BREAK_STATEMENT = register("break_statement", new StatementType<>(BreakStatement.CODEC));
    public static final StatementType<ContinueStatement> CONTINUE_STATEMENT = register("continue_statement", new StatementType<>(ContinueStatement.CODEC));
    public static final StatementType<ExpressionStatement> EXPRESSION_STATEMENT = register("expression_statement", new StatementType<>(ExpressionStatement.CODEC));
    public static final StatementType<ForStatement> FOR_STATEMENT = register("for_statement", new StatementType<>(ForStatement.CODEC));
    public static final StatementType<FunctionDeclaration> FUNCTION_DECLARATION = register("function_declaration", new StatementType<>(FunctionDeclaration.CODEC));
    public static final StatementType<IfStatement> IF_STATEMENT = register("if_statement", new StatementType<>(IfStatement.CODEC));
    public static final StatementType<ImportStatement> IMPORT_STATEMENT = register("import_statement", new StatementType<>(ImportStatement.CODEC));
    public static final StatementType<ReturnStatement> RETURN_STATEMENT = register("return_statement", new StatementType<>(ReturnStatement.CODEC));
    public static final StatementType<WhileStatement> WHILE_STATEMENT = register("while_statement", new StatementType<>(WhileStatement.CODEC));

    public static <T extends Statement> StatementType<T> register(String id, StatementType<T> statementType) {
        return Registry.register(StatementType.REGISTRY, Identifier.of("blogic", id), statementType);
    }

    public static void initialize() {
    }
}
