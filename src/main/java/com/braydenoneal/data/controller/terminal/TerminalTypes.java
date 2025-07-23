package com.braydenoneal.data.controller.terminal;

import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.braydenoneal.data.controller.terminal.types.ErrorTerminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TerminalTypes {
    public static final TerminalType<BooleanTerminal> BOOLEAN_TERMINAL = register("bool", new TerminalType<>(BooleanTerminal.CODEC));
    public static final TerminalType<IntegerTerminal> INTEGER_TERMINAL = register("int", new TerminalType<>(IntegerTerminal.CODEC));
    public static final TerminalType<ErrorTerminal> ERROR_TERMINAL = register("error", new TerminalType<>(ErrorTerminal.CODEC));
    public static final TerminalType<VoidTerminal> VOID_TERMINAL = register("void", new TerminalType<>(VoidTerminal.CODEC));

    public static <T extends Terminal> TerminalType<T> register(String id, TerminalType<T> terminalType) {
        return Registry.register(TerminalType.REGISTRY, Identifier.of("blogic", id), terminalType);
    }
}
