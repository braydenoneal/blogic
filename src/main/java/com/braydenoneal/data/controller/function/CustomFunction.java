package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.datafixers.util.Either;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class CustomFunction {
    String name;
    Class<? extends Terminal> returnClass;
    Map<String, Class<? extends Terminal>> parameterTypes;
    List<Function> body;

    public CustomFunction(String name, Class<? extends Terminal> returnClass, Map<String, Class<? extends Terminal>> parameterTypes, List<Function> body) {
        this.name = name;
        this.returnClass = returnClass;
        this.parameterTypes = parameterTypes;
        this.body = body;
    }

    public Terminal call(World world, BlockPos pos, Map<String, Either<Terminal, Function>> parameters, Map<String, Terminal> variables) throws Exception {
        Map<String, Terminal> variablesWithParameters = new java.util.HashMap<>(variables);

        for (Map.Entry<String, Either<Terminal, Function>> entry : parameters.entrySet()) {
            Terminal terminal = Terminal.getTerminal(world, pos, Map.of(), entry.getValue());
            variablesWithParameters.put(entry.getKey(), terminal);
        }

        Terminal returnValue = new VoidTerminal();

        for (Function function : body) {
            returnValue = function.call(world, pos, variablesWithParameters);
        }

        return returnValue;
    }
}
