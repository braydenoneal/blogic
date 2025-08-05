package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class ConditionalFunction implements Function {
    public static final MapCodec<ConditionalFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC)
                            .fieldOf("predicate").forGetter(ConditionalFunction::predicate),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("a").forGetter(ConditionalFunction::a),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("b").forGetter(ConditionalFunction::b)
            ).apply(instance, ConditionalFunction::new)
    );

    private Either<Terminal, Function> predicate;
    private Either<Terminal, Function> a;
    private Either<Terminal, Function> b;

    public ConditionalFunction(Either<Terminal, Function> predicate, Either<Terminal, Function> a, Either<Terminal, Function> b) {
        this.predicate = predicate;
        this.a = a;
        this.b = b;
    }

    public Either<Terminal, Function> predicate() {
        return predicate;
    }

    public Either<Terminal, Function> a() {
        return a;
    }

    public Either<Terminal, Function> b() {
        return b;
    }

    @Override
    public Terminal method(Context context) throws Exception {
        // TODO: Generalize the type here somehow
        return new IntegerTerminal(BooleanTerminal.getValue(context, predicate) ? IntegerTerminal.getValue(context, a) : IntegerTerminal.getValue(context, b));
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("if"),
                new ParameterGuiComponent("predicate", predicate),
                new LabelGuiComponent("return"),
                new ParameterGuiComponent("a", a),
                new LabelGuiComponent("else"),
                new ParameterGuiComponent("b", b)
        );
    }

    @Override
    public Function withParam(String name, Either<Terminal, Function> value) {
        return switch (name) {
            case "predicate" -> new ConditionalFunction(value, a, b);
            case "a" -> new ConditionalFunction(predicate, value, b);
            case "b" -> new ConditionalFunction(predicate, a, value);
            default -> this;
        };
    }

    @Override
    public void setParameter(String name, Either<Terminal, Function> value) {
        switch (name) {
            case "predicate" -> predicate = value;
            case "a" -> a = value;
            case "b" -> b = value;
        }
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.CONDITIONAL_FUNCTION;
    }
}
