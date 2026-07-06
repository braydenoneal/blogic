package com.braydenoneal.blang.wrapper.codec.expression

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import parser.expression.*
import parser.expression.builtin.*
import parser.expression.builtin.list.*
import parser.expression.builtin.struct.*
import parser.expression.operator.*

object ExpressionCodecs {
    val ARGUMENTS_CODEC: Codec<Arguments> = RecordCodecBuilder.create {
        it.group(
            Codec.list(ExpressionType.CODEC).fieldOf("arguments").forGetter(Arguments::arguments),
            Codec.unboundedMap(Codec.STRING, ExpressionType.CODEC).fieldOf("namedArguments").forGetter(Arguments::namedArguments)
        ).apply(it, ::Arguments)
    }
    val ASSIGNMENT_EXPRESSION_CODEC: MapCodec<AssignmentExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.STRING.fieldOf("type").forGetter(AssignmentExpression::operator),
            ExpressionType.CODEC.fieldOf("variable_expression").forGetter(AssignmentExpression::variableExpression),
            ExpressionType.CODEC.fieldOf("expression").forGetter(AssignmentExpression::variableExpression)
        ).apply(it, ::AssignmentExpression)
    }
    val IF_ELSE_EXPRESSION_CODEC: MapCodec<IfElseExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(IfElseExpression::condition),
            ExpressionType.CODEC.fieldOf("expression_a").forGetter(IfElseExpression::expressionA),
            ExpressionType.CODEC.fieldOf("expression_b").forGetter(IfElseExpression::expressionB)
        ).apply(it, ::IfElseExpression)
    }
    val LIST_ACCESS_EXPRESSION_CODEC: MapCodec<ListAccessExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("listExpression").forGetter(ListAccessExpression::listExpression),
            Codec.list(ExpressionType.CODEC).fieldOf("indices").forGetter(ListAccessExpression::indices)
        ).apply(it, ::ListAccessExpression)
    }
    val LIST_EXPRESSION_CODEC: MapCodec<ListExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.list(ExpressionType.CODEC).fieldOf("expressions").forGetter(ListExpression::expressions)
        ).apply(it, ::ListExpression)
    }
    val MEMBER_CALL_EXPRESSION_CODEC: MapCodec<MemberCallExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("member").forGetter(MemberCallExpression::member),
            Codec.STRING.fieldOf("functionName").forGetter(MemberCallExpression::functionName),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(MemberCallExpression::arguments)
        ).apply(it, ::MemberCallExpression)
    }
    val CALL_EXPRESSION_CODEC: MapCodec<CallExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(CallExpression::name),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(CallExpression::arguments)
        ).apply(it, ::CallExpression)
    }
    val MEMBER_EXPRESSION_CODEC: MapCodec<MemberExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("member").forGetter(MemberExpression::member),
            Codec.STRING.fieldOf("property").forGetter(MemberExpression::property)
        ).apply(it, ::MemberExpression)
    }
    val NAMED_LIST_ACCESS_EXPRESSION_CODEC: MapCodec<NamedListAccessExpression> = RecordCodecBuilder.mapCodec(Function {
        it.group(
            ExpressionType.CODEC.fieldOf("variableExpression").forGetter(NamedListAccessExpression::variableExpression),
            Codec.list(ExpressionType.CODEC).fieldOf("indices").forGetter(NamedListAccessExpression::indices)
        ).apply(it, ::NamedListAccessExpression)
    })
    val STRUCT_EXPRESSION_CODEC: MapCodec<StructExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.list(Codec.pair(Codec.STRING, ExpressionType.CODEC)).fieldOf("expressions").forGetter(StructExpression::expressions)
        ).apply(it, ::StructExpression)
    }
    val VARIABLE_EXPRESSION_CODEC: MapCodec<VariableExpression> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(VariableExpression::name)
        ).apply(it, ::VariableExpression)
    }
    val ARITHMETIC_OPERATOR_CODEC: MapCodec<ArithmeticOperator> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(ArithmeticOperator::operator),
            ExpressionType.CODEC.fieldOf("operand_a").forGetter(ArithmeticOperator::operandA),
            ExpressionType.CODEC.fieldOf("operand_b").forGetter(ArithmeticOperator::operandB)
        ).apply(it, ::ArithmeticOperator)
    }
    val BOOLEAN_OPERATOR_CODEC: MapCodec<BooleanOperator> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(BooleanOperator::operator),
            ExpressionType.CODEC.fieldOf("operand_a").forGetter(BooleanOperator::operandA),
            ExpressionType.CODEC.fieldOf("operand_b").forGetter(BooleanOperator::operandB)
        ).apply(it, ::BooleanOperator)
    }
    val COMPARISON_OPERATOR_CODEC: MapCodec<ComparisonOperator> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(ComparisonOperator::operator),
            ExpressionType.CODEC.fieldOf("operand_a").forGetter(ComparisonOperator::operandA),
            ExpressionType.CODEC.fieldOf("operand_b").forGetter(ComparisonOperator::operandB)
        ).apply(it, ::ComparisonOperator)
    }
    val UNARY_OPERATOR_CODEC: MapCodec<UnaryOperator> = RecordCodecBuilder.mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("operand").forGetter(UnaryOperator::operand)
        ).apply(it, ::UnaryOperator)
    }
    val ABSOLUTE_VALUE_BUILTIN_CODEC: MapCodec<AbsoluteValueBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(AbsoluteValueBuiltin::arguments)
        ).apply(it, ::AbsoluteValueBuiltin)
    }
    val CEIL_BUILTIN_CODEC: MapCodec<CeilBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(CeilBuiltin::arguments)
        ).apply(it, ::CeilBuiltin)
    }
    val FLOAT_CAST_BUILTIN_CODEC: MapCodec<FloatCastBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(FloatCastBuiltin::arguments)
        ).apply(it, ::FloatCastBuiltin)
    }
    val FLOOR_BUILTIN_CODEC: MapCodec<FloorBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(FloorBuiltin::arguments)
        ).apply(it, ::FloorBuiltin)
    }
    val INTEGER_CAST_BUILTIN_CODEC: MapCodec<IntegerCastBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(IntegerCastBuiltin::arguments)
        ).apply(it, ::IntegerCastBuiltin)
    }
    val LENGTH_BUILTIN_CODEC: MapCodec<LengthBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(LengthBuiltin::arguments)
        ).apply(it, ::LengthBuiltin)
    }
    val MAXIMUM_BUILTIN_CODEC: MapCodec<MaximumBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(MaximumBuiltin::arguments)
        ).apply(it, ::MaximumBuiltin)
    }
    val MINIMUM_BUILTIN_CODEC: MapCodec<MinimumBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(MinimumBuiltin::arguments)
        ).apply(it, ::MinimumBuiltin)
    }
    val PRINT_BUILTIN_CODEC: MapCodec<PrintBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(PrintBuiltin::arguments)
        ).apply(it, ::PrintBuiltin)
    }
    val RANGE_BUILTIN_CODEC: MapCodec<RangeBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(RangeBuiltin::arguments)
        ).apply(it, ::RangeBuiltin)
    }
    val ROUND_BUILTIN_CODEC: MapCodec<RoundBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(RoundBuiltin::arguments)
        ).apply(it, ::RoundBuiltin)
    }
    val STRING_CAST_BUILTIN_CODEC: MapCodec<StringCastBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StringCastBuiltin::arguments)
        ).apply(it, ::StringCastBuiltin)
    }
    val TYPE_BUILTIN_CODEC: MapCodec<TypeBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(TypeBuiltin::arguments)
        ).apply(it, ::TypeBuiltin)
    }
    val LIST_APPEND_BUILTIN_CODEC: MapCodec<ListAppendBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ListValue.CODEC.fieldOf("listValue").forGetter(ListAppendBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListAppendBuiltin::arguments)
        ).apply(it, ::ListAppendBuiltin)
    }
    val LIST_CONTAINS_ALL_BUILTIN_CODEC: MapCodec<ListContainsAllBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ListValue.CODEC.fieldOf("listValue").forGetter(ListContainsAllBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListContainsAllBuiltin::arguments)
        ).apply(it, ::ListContainsAllBuiltin)
    }
    val LIST_CONTAINS_BUILTIN_CODEC: MapCodec<ListContainsBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ListValue.CODEC.fieldOf("listValue").forGetter(ListContainsBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListContainsBuiltin::arguments)
        ).apply(it, ::ListContainsBuiltin)
    }
    val LIST_INSERT_BUILTIN_CODEC: MapCodec<ListInsertBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ListValue.CODEC.fieldOf("listValue").forGetter(ListInsertBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListInsertBuiltin::arguments)
        ).apply(it, ::ListInsertBuiltin)
    }
    val LIST_POP_BUILTIN_CODEC: MapCodec<ListPopBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ListValue.CODEC.fieldOf("listValue").forGetter(ListPopBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListPopBuiltin::arguments)
        ).apply(it, ::ListPopBuiltin)
    }
    val LIST_REMOVE_BUILTIN_CODEC: MapCodec<ListRemoveBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            ListValue.CODEC.fieldOf("listValue").forGetter(ListRemoveBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListRemoveBuiltin::arguments)
        ).apply(it, ::ListRemoveBuiltin)
    }
    val STRUCT_ENTRIES_BUILTIN_CODEC: MapCodec<StructEntriesBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            StructValue.CODEC.fieldOf("struct").forGetter(StructEntriesBuiltin::struct),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructEntriesBuiltin::arguments)
        ).apply(it, ::StructEntriesBuiltin)
    }
    val STRUCT_KEYS_BUILTIN_CODEC: MapCodec<StructKeysBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            StructValue.CODEC.fieldOf("struct").forGetter(StructKeysBuiltin::struct),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructKeysBuiltin::arguments)
        ).apply(it, ::StructKeysBuiltin)
    }
    val STRUCT_REMOVE_BUILTIN_CODEC: MapCodec<StructRemoveBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            StructValue.CODEC.fieldOf("struct").forGetter(StructRemoveBuiltin::struct),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructRemoveBuiltin::arguments)
        ).apply(it, ::StructRemoveBuiltin)
    }
    val STRUCT_VALUES_BUILTIN_CODEC: MapCodec<StructValuesBuiltin> = RecordCodecBuilder.mapCodec {
        it.group(
            StructValue.CODEC.fieldOf("struct").forGetter(StructValuesBuiltin::struct),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructValuesBuiltin::arguments)
        ).apply(it, ::StructValuesBuiltin)
    }
}
