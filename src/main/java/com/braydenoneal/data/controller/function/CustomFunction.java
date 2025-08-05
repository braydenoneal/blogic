package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.function.types.SetVariableFunction;
import com.braydenoneal.data.controller.parameter.Parameter;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.ErrorTerminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CustomFunction(
        String name,
        Parameter returnType,
        Map<String, Parameter> parameterTypes,
        List<Function> body
) {
    public static final Codec<CustomFunction> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(CustomFunction::name),
                    Parameter.CODEC.fieldOf("return_type").forGetter(CustomFunction::returnType),
                    Codec.unboundedMap(Codec.STRING, Parameter.CODEC)
                            .fieldOf("parameter_types").forGetter(CustomFunction::parameterTypes),
                    Codec.list(Function.CODEC).fieldOf("body").forGetter(CustomFunction::body)
            ).apply(instance, CustomFunction::new)
    );

    public Terminal call(Context context, Map<String, Either<Terminal, Function>> parameters) {
        Map<String, Terminal> variablesWithParameters = new HashMap<>(context.variables());

        // TODO: Check if parameters has all variables in parameterTypes
        for (Map.Entry<String, Either<Terminal, Function>> entry : parameters.entrySet()) {
            Parameter parameter = parameterTypes.get(entry.getKey());

            if (parameter == null) {
                return new ErrorTerminal("Parameter " + entry.getKey() + " does not exist");
            }

            Terminal terminal;

            try {
                terminal = Terminal.getTerminal(context, entry.getValue());
            } catch (Exception e) {
                return new ErrorTerminal("Error in " + entry.getKey() + " parameter");
            }

            if (!parameter.matchesTerminal(terminal)) {
                return new ErrorTerminal("Parameter " + entry.getKey() + " is incorrect type");
            }

            variablesWithParameters.put(entry.getKey(), terminal);
        }

        Context newContext = new Context(context.world(), context.pos(), variablesWithParameters);
        Terminal returnValue = new VoidTerminal();

        for (Function function : body) {
            returnValue = function.call(newContext);

            if (function instanceof SetVariableFunction setVariableFunction) {
                try {
                    newContext.variables().put(setVariableFunction.name(), Terminal.getTerminal(context, setVariableFunction.value()));
                } catch (Exception e) {
                    return new ErrorTerminal("Error setting variable");
                }
            } else if (returnValue instanceof ErrorTerminal) {
                return returnValue;
            }
        }

        if (!returnType.matchesTerminal(returnValue)) {
            return new ErrorTerminal("Incorrect return type");
        }

        return returnValue;
    }
}
