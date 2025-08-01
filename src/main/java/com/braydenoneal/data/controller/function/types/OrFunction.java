package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

public record OrFunction(Either<Terminal, Function> a, Either<Terminal, Function> b) implements Function {
    public static final MapCodec<OrFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("a").forGetter(OrFunction::a),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("b").forGetter(OrFunction::b)
            ).apply(instance, OrFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        return new BooleanTerminal(BooleanTerminal.getValue(context, a) || BooleanTerminal.getValue(context, b));
    }

    @Override
    public String getName() {
        return "Or";
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return Map.of("a", a, "b", b);
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.OR_FUNCTION;
    }
}
