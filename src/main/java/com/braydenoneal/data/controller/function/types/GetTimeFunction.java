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

import java.util.List;
import java.util.Map;

public record GetTimeFunction() implements Function {
    public static final MapCodec<GetTimeFunction> CODEC = MapCodec.assumeMapUnsafe(Codec.unit(new GetTimeFunction()));

    @Override
    public Terminal method(Context context) throws Exception {
        return new IntegerTerminal((int) context.world().getTime());
    }

    @Override
    public String getName() {
        return "Get Time";
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return Map.of();
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("get time")
        );
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.GET_TIME_FUNCTION;
    }
}
