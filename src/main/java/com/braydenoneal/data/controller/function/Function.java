package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.ErrorTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

import java.util.List;

// TODO: Have functions specify their return types and parameter types so that errors can be detected before running
// TODO: Wrap Either<Terminal, Function>
public interface Function {
    Codec<Function> CODEC = FunctionType.REGISTRY.getCodec().dispatch("function_type", Function::getType, FunctionType::codec);

    default Terminal call(Context context) {
        try {
            return method(context);
        } catch (Exception e) {
            return new ErrorTerminal(e.getMessage());
        }
    }

    Terminal method(Context context) throws Exception;

    List<GuiComponent> getGuiComponents();

    FunctionType<?> getType();

    interface GuiComponent {
    }

    record ParameterGuiComponent(Either<Terminal, Function> parameter) implements GuiComponent {
    }

    record LabelGuiComponent(String text) implements GuiComponent {
    }

    record TextFieldGuiComponent(String value) implements GuiComponent {
    }
}
