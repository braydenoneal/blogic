package com.braydenoneal.component;

import com.braydenoneal.Blogic;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static final ComponentType<String> NAME_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Blogic.MOD_ID, "name"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static void initialize() {
    }
}
