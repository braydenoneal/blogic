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
import java.util.Map;

public record AndFunction(Either<Terminal, Function> a, Either<Terminal, Function> b) implements Function {
    public static final MapCodec<AndFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("a").forGetter(AndFunction::a),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("b").forGetter(AndFunction::b)
            ).apply(instance, AndFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        return new BooleanTerminal(BooleanTerminal.getValue(context, a) && BooleanTerminal.getValue(context, b));
    }

    @Override
    public String getName() {
        return "And";
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return Map.of("a", a, "b", b);
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new ParameterGuiComponent(a),
                new LabelGuiComponent("and"),
                new ParameterGuiComponent(b)
        );
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.AND_FUNCTION;
    }
}
