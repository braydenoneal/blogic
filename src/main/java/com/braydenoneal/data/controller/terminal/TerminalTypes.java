package com.braydenoneal.data.controller.terminal;

import com.braydenoneal.data.controller.terminal.types.*;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TerminalTypes {
    public static final TerminalType<BooleanTerminal> BOOLEAN_TERMINAL = register("boolean", new TerminalType<>(BooleanTerminal.CODEC));
    public static final TerminalType<IntegerTerminal> INTEGER_TERMINAL = register("integer", new TerminalType<>(IntegerTerminal.CODEC));
    public static final TerminalType<ListTerminal> LIST_TERMINAL = register("list", new TerminalType<>(ListTerminal.CODEC));
    public static final TerminalType<VoidTerminal> VOID_TERMINAL = register("void", new TerminalType<>(VoidTerminal.CODEC));
    public static final TerminalType<ErrorTerminal> ERROR_TERMINAL = register("error", new TerminalType<>(ErrorTerminal.CODEC));

    public static <T extends Terminal> TerminalType<T> register(String id, TerminalType<T> terminalType) {
        return Registry.register(TerminalType.REGISTRY, Identifier.of("blogic", id), terminalType);
    }
}
