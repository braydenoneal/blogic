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

public class SetGlobalVariableFunction implements Function {
    public static final MapCodec<SetGlobalVariableFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(SetGlobalVariableFunction::name),
                    Codec.either(Terminal.CODEC, Function.CODEC)
                            .fieldOf("value").forGetter(SetGlobalVariableFunction::value),
                    Codec.either(Terminal.CODEC, Function.CODEC)
                            .fieldOf("predicate").forGetter(SetGlobalVariableFunction::predicate)
            ).apply(instance, SetGlobalVariableFunction::new)
    );

    private String name;
    private Either<Terminal, Function> value;
    private Either<Terminal, Function> predicate;

    public SetGlobalVariableFunction(String name, Either<Terminal, Function> value, Either<Terminal, Function> predicate) {
        this.name = name;
        this.value = value;
        this.predicate = predicate;
    }

    public String name() {
        return name;
    }

    public Either<Terminal, Function> value() {
        return value;
    }

    public Either<Terminal, Function> predicate() {
        return predicate;
    }

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
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("set global"),
                new TextFieldGuiComponent(name),
                new ParameterGuiComponent("value", value),
                new LabelGuiComponent("if"),
                new ParameterGuiComponent("predicate", predicate)
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
        switch (name) {
            case "value" -> this.value = value;
            case "predicate" -> predicate = value;
        }
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.SET_GLOBAL_VARIABLE_FUNCTION;
    }
}
