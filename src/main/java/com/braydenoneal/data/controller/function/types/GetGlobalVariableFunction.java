package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.block.entity.ControllerBlockEntity;
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
import net.minecraft.block.entity.BlockEntity;

import java.util.List;

public record GetGlobalVariableFunction(String name) implements Function {
    public static final MapCodec<GetGlobalVariableFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(GetGlobalVariableFunction::name)
            ).apply(instance, GetGlobalVariableFunction::new)
    );

    @Override
    public Terminal method(Context context) {
        BlockEntity blockEntity = context.world().getBlockEntity(context.pos());

        if (blockEntity instanceof ControllerBlockEntity controllerBlockEntity) {
            Terminal terminal = controllerBlockEntity.getVariables().get(name);

            if (terminal != null) {
                return terminal;
            }
        }

        return new ErrorTerminal("Variable does not exist");
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("get global"),
                new TextFieldGuiComponent(name)
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
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.GET_GLOBAL_VARIABLE_FUNCTION;
    }
}
