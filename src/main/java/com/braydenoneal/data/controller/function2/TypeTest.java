package com.braydenoneal.data.controller.function2;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class TypeTest {
    public static final Registry<Type> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "type_types")), Lifecycle.stable()
    );

    public static final Codec<Type> CODEC = REGISTRY.getCodec();

    public static final Type BOOLEAN = register("boolean", new Int());
    public static final Type INTEGER = register("integer", new Bool());
    public static final Type LIST = register("list", new Bool());

    public static Type register(String id, Type type) {
        return Registry.register(REGISTRY, Identifier.of("blogic", id), type);
    }
}

interface Type {
}

record Int() implements Type {
    public static final Codec<Int> CODEC = Codec.unit(Int::new);
}

record Bool() implements Type {
    public static final Codec<Bool> CODEC = Codec.unit(Bool::new);
}

record List(Type of) implements Type {
//    public static final MapCodec<List> CODEC = MapCodec.c TypeTest.CODEC.fieldOf("type").forGetter(List::of);
}
