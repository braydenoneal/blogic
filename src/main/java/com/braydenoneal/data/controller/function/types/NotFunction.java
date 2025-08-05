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

public class NotFunction implements Function {
    public static final MapCodec<NotFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("input").forGetter(NotFunction::input)
            ).apply(instance, NotFunction::new)
    );

    private Either<Terminal, Function> input;

    public NotFunction(Either<Terminal, Function> input) {
        this.input = input;
    }

    public Either<Terminal, Function> input() {
        return input;
    }

    @Override
    public Terminal method(Context context) throws Exception {
        return new BooleanTerminal(!BooleanTerminal.getValue(context, input));
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("not"),
                new ParameterGuiComponent("input", input)
        );
    }

    @Override
    public Function withParam(String name, Either<Terminal, Function> value) {
        return new NotFunction(value);
    }

    @Override
    public void setParameter(String name, Either<Terminal, Function> value) {
        input = value;
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.NOT_FUNCTION;
    }
}
