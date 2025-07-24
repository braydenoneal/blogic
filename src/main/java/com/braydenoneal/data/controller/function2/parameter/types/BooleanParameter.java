package com.braydenoneal.data.controller.function2.parameter.types;

import com.braydenoneal.data.controller.function2.parameter.Parameter;
import com.braydenoneal.data.controller.function2.parameter.ParameterType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record BooleanParameter() implements Parameter {
    @Override
    public ParameterType<?> getType() {
        return null;
    }
}

abstract class Par {
    static String toString2(Par par) {
        return "";
    }
}

class Chi1 extends Par {
    static String toString2(Par par) {
        return "chi1";
    }
}

class Chi2 extends Par {
}

class Bruh {
    static void bruh() {
        Codec<Par> codec = Codec.STRING.xmap(
                s -> switch (s) {
                    case "chi1" -> new Chi1();
                    case "chi2" -> new Chi2();
                    default -> new Chi1();
                },
                Par::toString2
        );

//        Codec<Class<?>> classCodec = Codec.STRING.xmap(
//                s -> switch (s) {
//                    case "integer" -> Integer.class;
//                    case "string" -> String.class;
//                    default -> Boolean.class;
//                },
//                c -> switch (c) {
//                    case Integer.class -> "integer";
//                    case String.class -> "string";
//                    default -> Boolean.class;
//                }
//        );

        Registry<Par> parRegistry = new SimpleRegistry<>(
                RegistryKey.ofRegistry(Identifier.of("blogic", "par_types")), Lifecycle.stable()
        );
        Registry.register(parRegistry, Identifier.of("blogic", "chi1"), new Chi1());
        Codec<Par> parCodec = parRegistry.getCodec();
    }
}
