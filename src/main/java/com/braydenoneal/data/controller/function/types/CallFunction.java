package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.function.Parameter;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

// TODO: Implement
public record CallFunction(String name, String returnType, Either<Terminal, Function> body) implements Function {
    public static final MapCodec<CallFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(CallFunction::name),
            Codec.STRING.fieldOf("returnType").forGetter(CallFunction::returnType),
            Parameter.CODEC.fieldOf("body").forGetter(CallFunction::body)
    ).apply(instance, CallFunction::new));

    @Override
    public Terminal method(World world, BlockPos pos, Map<String, Terminal> variables) throws Exception {
        return new BooleanTerminal(false);
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.NOT_FUNCTION;
    }
}
