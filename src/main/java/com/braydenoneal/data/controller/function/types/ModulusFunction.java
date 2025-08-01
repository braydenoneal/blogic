package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;

public record ModulusFunction(Either<Terminal, Function> a, Either<Terminal, Function> b) implements Function {
    public static final MapCodec<ModulusFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("a").forGetter(ModulusFunction::a),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("b").forGetter(ModulusFunction::b)
            ).apply(instance, ModulusFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        return new IntegerTerminal(IntegerTerminal.getValue(context, a) % IntegerTerminal.getValue(context, b));
    }

    @Override
    public String getName() {
        return "Modulus";
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return Map.of("a", a, "b", b);
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new ParameterGuiComponent(a),
                new LabelGuiComponent("%"),
                new ParameterGuiComponent(b)
        );
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.MODULUS_FUNCTION;
    }
}
