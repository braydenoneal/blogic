package blang.codec.expression

import blang.codec.Codecs.mutableListCodec
import blang.codec.Codecs.mutableMapCodec
import blang.codec.builtin.BuiltinType
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
        ).apply(it, ::Arguments)
    }
    val ASSIGN_EXPRESSION_CODEC: MapCodec<AssignExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(AssignExpression::operator),
            ExpressionType.CODEC.fieldOf("left").forGetter(AssignExpression::left),
            ExpressionType.CODEC.fieldOf("right").forGetter(AssignExpression::right),
        ).apply(it, ::AssignExpression)
    }
    val IF_ELSE_EXPRESSION_CODEC: MapCodec<IfElseExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(IfElseExpression::condition),
            ExpressionType.CODEC.fieldOf("expression_a").forGetter(IfElseExpression::expressionA),
            ExpressionType.CODEC.fieldOf("expression_b").forGetter(IfElseExpression::expressionB),
        ).apply(it, ::IfElseExpression)
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
    val CALL_EXPRESSION_CODEC: MapCodec<CallExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(CallExpression::left),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(CallExpression::arguments),
            ValueType.CODEC.optionalFieldOf("left_value").forGetter { callExpression -> Optional.ofNullable(callExpression.leftValue) },
            BuiltinType.CODEC.optionalFieldOf("builtin").forGetter { callExpression -> Optional.ofNullable(callExpression.builtin) },
        ).apply(it) { left, arguments, leftValue, builtin ->
            CallExpression(left, arguments, leftValue.orElse(null), builtin.orElse(null))
        }
    }
    val DOT_EXPRESSION_CODEC: MapCodec<DotExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(DotExpression::left),
            Codec.STRING.fieldOf("right").forGetter(DotExpression::right),
        ).apply(it, ::DotExpression)
    }
    val STRUCT_EXPRESSION_CODEC: MapCodec<StructExpression> = mapCodec {
        it.group(
            Codec.list(PairCodec.pair(Codec.STRING, ExpressionType.CODEC)).fieldOf("expressions").forGetter(StructExpression::expressions),
        ).apply(it, ::StructExpression)
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
