package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ConditionalFunction(
        Either<Terminal, Function> predicate,
        Either<Terminal, Function> a,
        Either<Terminal, Function> b
) implements Function {
    public static final MapCodec<ConditionalFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC)
                            .fieldOf("predicate").forGetter(ConditionalFunction::predicate),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("a").forGetter(ConditionalFunction::a),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("b").forGetter(ConditionalFunction::b)
            ).apply(instance, ConditionalFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        return new IntegerTerminal(BooleanTerminal.getValue(context, predicate) ? IntegerTerminal.getValue(context, a) : IntegerTerminal.getValue(context, b));
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.CONDITIONAL_FUNCTION;
    }
}
