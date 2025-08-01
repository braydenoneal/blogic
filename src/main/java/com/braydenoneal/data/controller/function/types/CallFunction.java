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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CallFunction(String name, Map<String, Either<Terminal, Function>> parameters) implements Function {
    public static final MapCodec<CallFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(CallFunction::name),
                    Codec.unboundedMap(
                            Codec.STRING,
                            Codec.either(Terminal.CODEC, Function.CODEC)
                    ).fieldOf("parameters").forGetter(CallFunction::parameters)
            ).apply(instance, CallFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        // TODO: Implement this (get custom function instance from the controller block entity)
        return new BooleanTerminal(false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return parameters;
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        List<GuiComponent> components = new ArrayList<>();

        components.add(new LabelGuiComponent("call"));
        components.add(new TextFieldGuiComponent(name));

        for (Map.Entry<String, Either<Terminal, Function>> parameter : parameters.entrySet()) {
            components.add(new LabelGuiComponent(parameter.getKey()));
            components.add(new ParameterGuiComponent(parameter.getValue()));
        }

        return components;
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.NOT_FUNCTION;
    }
}
