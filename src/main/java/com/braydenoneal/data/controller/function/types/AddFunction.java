package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

public record AddFunction(Either<Terminal, Function> a, Either<Terminal, Function> b) implements Function {
    public static final MapCodec<AddFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("a").forGetter(AddFunction::a),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("b").forGetter(AddFunction::b)
            ).apply(instance, AddFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        return new IntegerTerminal(IntegerTerminal.getValue(context, a) + IntegerTerminal.getValue(context, b));
    }

    @Override
    public String getName() {
        return "Add";
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return Map.of("a", a, "b", b);
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.ADD_FUNCTION;
    }
}
