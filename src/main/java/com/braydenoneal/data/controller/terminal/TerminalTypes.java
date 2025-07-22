package com.braydenoneal.data.controller.terminal;

import com.braydenoneal.data.controller.functionold.BoolTerminal;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TerminalTypes {
    public static final TerminalType<BoolTerminal> BOOL_TERMINAL = register("bool", new TerminalType<>(BoolTerminal.CODEC));
    public static final TerminalType<IntTerminal> INT_TERMINAL = register("int", new TerminalType<>(IntTerminal.CODEC));

    public static <T extends Terminal> TerminalType<T> register(String id, TerminalType<T> terminalType) {
        return Registry.register(TerminalType.REGISTRY, Identifier.of("blogic", id), terminalType);
    }
}
