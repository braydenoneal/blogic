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

import java.util.List;

public record LessThanFunction(Either<Terminal, Function> a, Either<Terminal, Function> b) implements Function {
    public static final MapCodec<LessThanFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("a").forGetter(LessThanFunction::a),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("b").forGetter(LessThanFunction::b)
            ).apply(instance, LessThanFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        return new BooleanTerminal(IntegerTerminal.getValue(context, a) < IntegerTerminal.getValue(context, b));
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new ParameterGuiComponent(a),
                new LabelGuiComponent("<"),
                new ParameterGuiComponent(b)
        );
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.LESS_THAN_FUNCTION;
    }
}
