package blang.codec.expression

import blang.codec.Codecs.FUNCTION_CODEC
import blang.codec.Codecs.mutableListCodec
import blang.codec.Codecs.mutableMapCodec
import blang.codec.expression.PairCodec.Companion.pair
import blang.codec.value.ValueType
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import program.expression.*
import java.util.*

object ExpressionCodecs {
    val ARGUMENTS_CODEC: Codec<Arguments> = RecordCodecBuilder.create {
        it.group(
            mutableListCodec(ExpressionType.CODEC).fieldOf("nameless_arguments").forGetter(Arguments::namelessArguments),
            mutableMapCodec(Codec.STRING, ExpressionType.CODEC).fieldOf("named_arguments").forGetter(Arguments::namedArguments),
            Codec.INT.fieldOf("index").forGetter(Arguments::index),
            mutableMapCodec(Codec.STRING, ValueType.CODEC).fieldOf("computed").forGetter(Arguments::computed),
            Codec.INT.fieldOf("counter").forGetter(Arguments::counter),
            Codec.BOOL.fieldOf("hasSelf").forGetter(Arguments::hasSelf),
        ).apply(it, ::Arguments)
    }
    val ASSIGN_EXPRESSION_CODEC: MapCodec<AssignExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(AssignExpression::operator),
            ExpressionType.CODEC.fieldOf("left").forGetter(AssignExpression::left),
            ExpressionType.CODEC.fieldOf("right").forGetter(AssignExpression::right),
            Codec.BOOL.fieldOf("local").forGetter(AssignExpression::local),
        ).apply(it, ::AssignExpression)
    }
    val IF_ELSE_EXPRESSION_CODEC: MapCodec<IfElseExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(IfElseExpression::condition),
            ExpressionType.CODEC.fieldOf("expression_a").forGetter(IfElseExpression::expressionA),
            ExpressionType.CODEC.fieldOf("expression_b").forGetter(IfElseExpression::expressionB),
        ).apply(it, ::IfElseExpression)
    }
    val INFIX_FUNCTION_EXPRESSION_CODEC: MapCodec<InfixFunctionExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(InfixFunctionExpression::name),
            ExpressionType.CODEC.fieldOf("left").forGetter(InfixFunctionExpression::left),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(InfixFunctionExpression::arguments),
            ValueType.CODEC.optionalFieldOf("left_value").forGetter { infixFunctionExpression -> Optional.ofNullable(infixFunctionExpression.leftValue) },
        ).apply(it) { name, left, arguments, leftValue -> InfixFunctionExpression(name, left, arguments, leftValue.orElse(null)) }
    }
    val ACCESS_EXPRESSION_CODEC: MapCodec<AccessExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(AccessExpression::left),
            ExpressionType.CODEC.fieldOf("right").forGetter(AccessExpression::right),
        ).apply(it, ::AccessExpression)
    }
    val LIST_EXPRESSION_CODEC: MapCodec<ListExpression> = mapCodec {
        it.group(
            mutableListCodec(ExpressionType.CODEC).fieldOf("expressions").forGetter(ListExpression::expressions),
        ).apply(it, ::ListExpression)
    }
    val STRING_EXPRESSION_CODEC: MapCodec<StringExpression> = mapCodec {
        it.group(
            mutableListCodec(pair(Codec.STRING, ExpressionType.CODEC)).fieldOf("string_expression_pairs").forGetter(StringExpression::stringExpressionPairs),
            Codec.STRING.fieldOf("final_string").forGetter(StringExpression::finalString),
            mutableListCodec(Codec.STRING).fieldOf("values").forGetter(StringExpression::values),
        ).apply(it, ::StringExpression)
    }
    val SLICE_EXPRESSION_CODEC: MapCodec<SliceExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(SliceExpression::left),
            ExpressionType.CODEC.optionalFieldOf("from").forGetter { sliceExpression -> Optional.ofNullable(sliceExpression.from) },
            ExpressionType.CODEC.optionalFieldOf("to").forGetter { sliceExpression -> Optional.ofNullable(sliceExpression.to) },
        ).apply(it) { left, from, to -> SliceExpression(left, from.orElse(null), to.orElse(null)) }
    }
    val CALL_EXPRESSION_CODEC: MapCodec<CallExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(CallExpression::left),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(CallExpression::arguments),
            ValueType.CODEC.optionalFieldOf("left_value").forGetter { callExpression -> Optional.ofNullable(callExpression.leftValue) },
        ).apply(it) { left, arguments, leftValue -> CallExpression(left, arguments, leftValue.orElse(null)) }
    }
    val GET_EXPRESSION_CODEC: MapCodec<GetExpression> = mapCodec {
        it.group(
            FUNCTION_CODEC.fieldOf("function").forGetter(GetExpression::function),
        ).apply(it, ::GetExpression)
    }
    val DOT_EXPRESSION_CODEC: MapCodec<DotExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(DotExpression::left),
            Codec.STRING.fieldOf("right").forGetter(DotExpression::right),
        ).apply(it, ::DotExpression)
    }
    val IDENTIFIER_EXPRESSION_CODEC: MapCodec<IdentifierExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(IdentifierExpression::name),
        ).apply(it, ::IdentifierExpression)
    }
    val BINARY_OPERATOR_EXPRESSION_CODEC: MapCodec<BinaryOperatorExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(BinaryOperatorExpression::operator),
            ExpressionType.CODEC.fieldOf("left").forGetter(BinaryOperatorExpression::left),
            ExpressionType.CODEC.fieldOf("right").forGetter(BinaryOperatorExpression::right),
        ).apply(it, ::BinaryOperatorExpression)
    }
    val UNARY_OPERATOR_EXPRESSION_CODEC: MapCodec<UnaryOperatorExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(UnaryOperatorExpression::operator),
            ExpressionType.CODEC.fieldOf("operand").forGetter(UnaryOperatorExpression::operand),
        ).apply(it, ::UnaryOperatorExpression)
    }
}
