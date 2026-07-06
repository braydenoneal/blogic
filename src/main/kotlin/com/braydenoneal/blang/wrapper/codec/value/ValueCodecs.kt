package com.braydenoneal.blang.wrapper.codec.value

import com.braydenoneal.blang.wrapper.codec.expression.ExpressionType
import com.braydenoneal.blang.wrapper.codec.expression.PairCodec.Companion.pair
import com.braydenoneal.blang.wrapper.codec.statement.StatementCodecs.STATEMENT_LIST_CODEC
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import parser.expression.value.*

object ValueCodecs {
    val FUNCT_CODEC: Codec<Funct> = RecordCodecBuilder.create {
        it.group(
            Codec.list(Codec.STRING).fieldOf("parameters").forGetter(Funct::parameters),
            Codec.list(pair(Codec.STRING, ExpressionType.CODEC)).fieldOf("defaultParameters").forGetter(Funct::defaultParameters),
            STATEMENT_LIST_CODEC.fieldOf("statements").forGetter(Funct::statements),
        ).apply(it, ::Funct)
    }
    val NULL_CODEC: Codec<Null> = Codec.unit(Null())
    val RANGE_CODEC: Codec<Range> = RecordCodecBuilder.create {
        it.group(
            Codec.INT.fieldOf("start").forGetter(Range::start),
            Codec.INT.fieldOf("end").forGetter(Range::end),
            Codec.INT.fieldOf("step").forGetter(Range::step),
        ).apply(it, ::Range)
    }

    val BOOLEAN_VALUE_CODEC: MapCodec<BooleanValue> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.BOOL.fieldOf("value").forGetter(BooleanValue::value),
        ).apply(it, ::BooleanValue)
    }
    val FLOAT_VALUE_CODEC: MapCodec<FloatValue> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.FLOAT.fieldOf("value").forGetter(FloatValue::value),
        ).apply(it, ::FloatValue)
    }
    val FUNCTION_VALUE_CODEC: MapCodec<FunctionValue> = RecordCodecBuilder.mapCodec {
        it.group(
            FUNCT_CODEC.fieldOf("value").forGetter(FunctionValue::value),
        ).apply(it, ::FunctionValue)
    }
    val INTEGER_VALUE_CODEC: MapCodec<IntegerValue> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.INT.fieldOf("value").forGetter(IntegerValue::value),
        ).apply(it, ::IntegerValue)
    }
    val LIST_VALUE_CODEC: MapCodec<ListValue> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.list(ValueType.CODEC).fieldOf("value").forGetter(ListValue::value),
        ).apply(it, ::ListValue)
    }
    val NULL_VALUE_CODEC: MapCodec<NullValue> = RecordCodecBuilder.mapCodec {
        it.group(
            NULL_CODEC.fieldOf("null").forGetter(NullValue::value),
        ).apply(it, ::NullValue)
    }
    val RANGE_VALUE_CODEC: MapCodec<RangeValue> = RecordCodecBuilder.mapCodec {
        it.group(
            RANGE_CODEC.fieldOf("value").forGetter(RangeValue::value),
        ).apply(it, ::RangeValue)
    }
    val STRING_VALUE_CODEC: MapCodec<StringValue> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.STRING.fieldOf("value").forGetter(StringValue::value),
        ).apply(it, ::StringValue)
    }
    val STRUCT_VALUE_CODEC: MapCodec<StructValue> = RecordCodecBuilder.mapCodec {
        it.group(
            Codec.list(pair(Codec.STRING, ValueType.CODEC)).fieldOf("value").forGetter(StructValue::value),
        ).apply(it, ::StructValue)
    }
}
