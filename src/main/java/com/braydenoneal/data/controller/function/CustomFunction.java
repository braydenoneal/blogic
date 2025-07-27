package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.parameter.Parameter;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;

public record CustomFunction(
        String name,
        Parameter returnType,
        Map<String, Parameter> parameterTypes,
        List<Function> body
) {
    public static final MapCodec<CustomFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(CustomFunction::name),
                    Parameter.CODEC.fieldOf("return_type").forGetter(CustomFunction::returnType),
                    Codec.unboundedMap(Codec.STRING, Parameter.CODEC)
                            .fieldOf("parameter_types").forGetter(CustomFunction::parameterTypes),
                    Codec.list(Function.CODEC).fieldOf("body").forGetter(CustomFunction::body)
            ).apply(instance, CustomFunction::new)
    );

    public Terminal call(Context context, Map<String, Either<Terminal, Function>> parameters) throws Exception {
        Map<String, Terminal> variablesWithParameters = new java.util.HashMap<>(context.variables());
        // TODO: Check parameters against parameterTypes

        for (Map.Entry<String, Either<Terminal, Function>> entry : parameters.entrySet()) {
            Terminal terminal = Terminal.getTerminal(context, entry.getValue());
            variablesWithParameters.put(entry.getKey(), terminal);
        }

        Context newContext = new Context(context.world(), context.pos(), variablesWithParameters);
        Terminal returnValue = new VoidTerminal();

        // TODO: Check last function against returnType
        for (Function function : body) {
            // TODO: If function is set variable, then update variables
            // TODO: If function returns error terminal, then idk do something
            returnValue = function.call(newContext);
        }

        return returnValue;
    }
}
