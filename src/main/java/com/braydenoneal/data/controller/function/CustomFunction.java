package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.parameter.Parameter;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public record CustomFunction(
        String name,
        Parameter returnType,
        Map<String, Parameter> parameterTypes,
        List<Function> body
) {
    public static final MapCodec<CustomFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(CustomFunction::name),
            Parameter.CODEC.fieldOf("return_type").forGetter(CustomFunction::returnType),
            Codec.unboundedMap(Codec.STRING, Parameter.CODEC).fieldOf("parameter_types").forGetter(CustomFunction::parameterTypes),
            Codec.list(Function.CODEC).fieldOf("body").forGetter(CustomFunction::body)
    ).apply(instance, CustomFunction::new));

    public Terminal call(World world, BlockPos pos, Map<String, Either<Terminal, Function>> parameters, Map<String, Terminal> variables) throws Exception {
        Map<String, Terminal> variablesWithParameters = new java.util.HashMap<>(variables);
        // TODO: Check parameters against parameterTypes

        for (Map.Entry<String, Either<Terminal, Function>> entry : parameters.entrySet()) {
            Terminal terminal = Terminal.getTerminal(world, pos, Map.of(), entry.getValue());
            variablesWithParameters.put(entry.getKey(), terminal);
        }

        Terminal returnValue = new VoidTerminal();

        // TODO: Check last function against returnType
        for (Function function : body) {
            returnValue = function.call(world, pos, variablesWithParameters);
        }

        return returnValue;
    }
}
