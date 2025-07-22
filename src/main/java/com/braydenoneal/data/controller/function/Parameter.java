package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public class Parameter {
    public static final Codec<Either<Terminal, Function>> CODEC = Codec.either(Terminal.CODEC, Function.CODEC);
}
