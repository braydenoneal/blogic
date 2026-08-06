package blang.expression.builtin

import blang.BlogicProgram
import blang.Context
import blang.expression.value.block.BlockValue
import blang.expression.value.item.ItemValue
import net.minecraft.core.BlockPos
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import program.Program
import program.expression.Arguments
import program.expression.BinaryOperatorExpression
import program.expression.IdentifierExpression
import program.expression.value.Callable
import program.expression.value.Value
import program.expression.value.booleanvalue.BooleanValue
import program.expression.value.function.Function
import program.expression.value.function.FunctionValue
import program.expression.value.get
import program.expression.value.integer.IntegerValue
import program.statement.ReturnStatement
import program.statement.StatementList

abstract class BlogicBuiltin : Callable {
    context(program: Program, arguments: Arguments)
    override fun innerCall(): Value<*> {
        val context = BlogicProgram.cast(program.actionProgram).context
        return context(context) { blogicCall() }
    }

    context(program: Program, arguments: Arguments, context: Context)
    abstract fun blogicCall(): Value<*>

    context(program: Program, arguments: Arguments, context: Context)
    fun getBlockPos(): BlockPos {
        val x = get<IntegerValue>("x").value
        val y = get<IntegerValue>("y").value
        val z = get<IntegerValue>("z").value
        return context.entity.blockPos.offset(x, y, z)
    }

    context(program: Program, arguments: Arguments)
    fun getPredicate(): FunctionValue {
        var predicate = arguments.getAny(
            "predicate",
            FunctionValue(
                Function(
                    mutableListOf("value"),
                    mutableListOf(),
                    StatementList(mutableListOf(ReturnStatement(BooleanValue(true)))),
                ),
            ),
        )

        if (predicate is BlockValue || predicate is ItemValue) {
            predicate = FunctionValue(
                Function(
                    mutableListOf("value"),
                    mutableListOf(),
                    StatementList(mutableListOf(ReturnStatement(BinaryOperatorExpression("==", IdentifierExpression("value"), predicate)))),
                ),
            )
        }

        return predicate.cast<FunctionValue>()
    }

    context(program: Program)
    fun getPredicateResult(block: Block, predicate: FunctionValue): Boolean {
        val predicateArguments = Arguments(mutableListOf(BlockValue(block)), mutableMapOf())
        return context(predicateArguments) { predicate.call().cast<BooleanValue>().value }
    }

    context(program: Program)
    fun getPredicateResult(item: Item, predicate: FunctionValue): Boolean {
        val predicateArguments = Arguments(mutableListOf(ItemValue(item)), mutableMapOf())
        return context(predicateArguments) { predicate.call().cast<BooleanValue>().value }
    }

    context(context: Context)
    fun getLevel(): Level {
        return context.entity.level!!
    }
}
