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

public class AndFunction implements Function {
    public static final MapCodec<AndFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("a").forGetter(AndFunction::a),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("b").forGetter(AndFunction::b)
            ).apply(instance, AndFunction::new)
    );

    private Either<Terminal, Function> a;
    private Either<Terminal, Function> b;

    public AndFunction(Either<Terminal, Function> a, Either<Terminal, Function> b) {
        this.a = a;
        this.b = b;
    }

    public Either<Terminal, Function> a() {
        return a;
    }

    public Either<Terminal, Function> b() {
        return b;
    }

    @Override
    public Terminal method(Context context) throws Exception {
        return new BooleanTerminal(BooleanTerminal.getValue(context, a) && BooleanTerminal.getValue(context, b));
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new ParameterGuiComponent("a", a),
                new LabelGuiComponent("and"),
                new ParameterGuiComponent("b", b)
        );
    }

    @Override
    public Function withParam(String name, Either<Terminal, Function> value) {
        return switch (name) {
            case "a" -> new AndFunction(value, b);
            case "b" -> new AndFunction(a, value);
            default -> this;
        };
    }

    @Override
    public void setParameter(String name, Either<Terminal, Function> value) {
        switch (name) {
            case "a" -> a = value;
            case "b" -> b = value;
        }
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.AND_FUNCTION;
    }
}
