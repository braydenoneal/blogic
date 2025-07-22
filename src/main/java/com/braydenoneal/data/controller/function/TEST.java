package com.braydenoneal.data.controller.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class TEST {
    public static void main(String[] args) {
//        ReadRedstoneIsHighFunction readRedstoneIsHighFunction = new ReadRedstoneIsHighFunction(Map.of(
//                "x", new IntegerTerminal(0),
//                "y", new IntegerTerminal(0),
//                "z", new IntegerTerminal(0)
//        ));
//
//        NotFunction notFunction = new NotFunction(Map.of(
//                "input", readRedstoneIsHighFunction.call()
//        ));
//
//        WriteRedstoneIsHighFunction writeRedstoneIsHighFunction = new WriteRedstoneIsHighFunction(Map.of(
//                "x", new IntegerTerminal(-2),
//                "y", new IntegerTerminal(0),
//                "z", new IntegerTerminal(0),
//                "high", notFunction.call()
//        ));
//
//        writeRedstoneIsHighFunction.call();

//        Codec<AbstractFunction> abstractFunctionCodec = Codec.recursive(
//                "abstractFunction",
//                selfCodec -> {
//                    return RecordCodecBuilder.create(instance ->
//                            instance.group(
//                                    Codec.STRING.fieldOf("function").forGetter(AbstractFunction::getName),
//                                    Codec.unboundedMap(
//                                            Codec.STRING,
//                                            Codec.either(
//                                                    selfCodec,
//                                                    Codec.unit(new AbstractTerminal())
//                                            )
//                                    ).fieldOf("parameters").forGetter(AbstractFunction::getParameters)
//                            ).apply(instance, AbstractFunction::new)
//                    );
//                }
//        );
    }
}

interface Terminal {
    Codec<Terminal> TERMINAL_CODEC = TerminalType.REGISTRY.getCodec().dispatch("type", Terminal::getType, TerminalType::codec);

    TerminalType<?> getType();
}

record TerminalType<T extends Terminal>(MapCodec<T> codec) {
    public static final Registry<TerminalType<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "terminal_types")), Lifecycle.stable()
    );
}

record BoolTerminal(boolean value) implements Terminal {
    public static final MapCodec<BoolTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("value").forGetter(BoolTerminal::value)
    ).apply(instance, BoolTerminal::new));

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.BOOL_TERMINAL;
    }
}

record IntTerminal(boolean value) implements Terminal {
    public static final MapCodec<IntTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("value").forGetter(IntTerminal::value)
    ).apply(instance, IntTerminal::new));

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.INT_TERMINAL;
    }
}

class TerminalTypes {
    public static final TerminalType<BoolTerminal> BOOL_TERMINAL = register("bool", new TerminalType<>(BoolTerminal.CODEC));
    public static final TerminalType<IntTerminal> INT_TERMINAL = register("int", new TerminalType<>(IntTerminal.CODEC));

    public static <T extends Terminal> TerminalType<T> register(String id, TerminalType<T> terminalType) {
        return Registry.register(TerminalType.REGISTRY, Identifier.of("blogic", id), terminalType);
    }
}
