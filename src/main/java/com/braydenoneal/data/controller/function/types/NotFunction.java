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

import java.util.List;

public record NotFunction(Either<Terminal, Function> input) implements Function {
    public static final MapCodec<NotFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("input").forGetter(NotFunction::input)
            ).apply(instance, NotFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        return new BooleanTerminal(!BooleanTerminal.getValue(context, input));
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("not"),
                new ParameterGuiComponent(input)
        );
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.NOT_FUNCTION;
    }
}
