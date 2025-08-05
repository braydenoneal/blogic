package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class SetVariableFunction implements Function {
    public static final MapCodec<SetVariableFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(SetVariableFunction::name),
                    Codec.either(Terminal.CODEC, Function.CODEC)
                            .fieldOf("value").forGetter(SetVariableFunction::value)
            ).apply(instance, SetVariableFunction::new)
    );

    private String name;
    private Either<Terminal, Function> value;

    public SetVariableFunction(String name, Either<Terminal, Function> value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public Either<Terminal, Function> value() {
        return value;
    }

    @Override
    public Terminal method(Context context) {
        return new VoidTerminal();
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("set"),
                new TextFieldGuiComponent(name),
                new ParameterGuiComponent("value", value)
        );
    }

    @Override
    public Function withParam(String name, Either<Terminal, Function> value) {
        return switch (name) {
            default -> this;
        };
    }

    @Override
    public void setParameter(String name, Either<Terminal, Function> value) {
        this.value = value;
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.SET_VARIABLE_FUNCTION;
    }
}
