package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.ErrorTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;

public record GetVariableFunction(String name) implements Function {
    public static final MapCodec<GetVariableFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(GetVariableFunction::name)
            ).apply(instance, GetVariableFunction::new)
    );

    @Override
    public Terminal method(Context context) {
        Terminal terminal = context.variables().get(name);

        if (terminal != null) {
            return terminal;
        }

        return new ErrorTerminal("Variable does not exist");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return Map.of();
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("get"),
                new TextFieldGuiComponent(name)
        );
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.GET_VARIABLE_FUNCTION;
    }
}
