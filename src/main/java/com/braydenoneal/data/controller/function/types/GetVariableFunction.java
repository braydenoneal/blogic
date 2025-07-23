package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.ErrorTerminal;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public record GetVariableFunction(String name) implements Function {
    public static final MapCodec<GetVariableFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(GetVariableFunction::name)
    ).apply(instance, GetVariableFunction::new));

    @Override
    public Terminal method(World world, BlockPos pos, Map<String, Terminal> variables) {
        Terminal terminal = variables.get(name);

        if (terminal != null) {
            return terminal;
        }

        return new ErrorTerminal("");
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.GET_VARIABLE_FUNCTION;
    }
}
