package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.block.entity.ControllerBlockEntity;
import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SetGlobalVariableFunction(
        String name,
        Either<Terminal, Function> value,
        Either<Terminal, Function> predicate
) implements Function {
    public static final MapCodec<SetGlobalVariableFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(SetGlobalVariableFunction::name),
                    Codec.either(Terminal.CODEC, Function.CODEC)
                            .fieldOf("value").forGetter(SetGlobalVariableFunction::value),
                    Codec.either(Terminal.CODEC, Function.CODEC)
                            .fieldOf("predicate").forGetter(SetGlobalVariableFunction::predicate)
            ).apply(instance, SetGlobalVariableFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        boolean predicateValue = BooleanTerminal.getValue(context, predicate);
        BlockEntity blockEntity = context.world().getBlockEntity(context.pos());

        if (predicateValue && blockEntity instanceof ControllerBlockEntity controllerBlockEntity) {
            Map<String, Terminal> variables = new HashMap<>(controllerBlockEntity.getVariables());
            variables.put(name, Terminal.getTerminal(context, value));
            controllerBlockEntity.setVariables(variables);
        }

        return new VoidTerminal();
    }

    @Override
    public String getName() {
        return "set global " + name;
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return Map.of("value", value, "predicate", predicate);
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("set global"),
                new TextFieldGuiComponent(name),
                new ParameterGuiComponent(value),
                new LabelGuiComponent("if"),
                new ParameterGuiComponent(predicate)
        );
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.SET_GLOBAL_VARIABLE_FUNCTION;
    }
}
