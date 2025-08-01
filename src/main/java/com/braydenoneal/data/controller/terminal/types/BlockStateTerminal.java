package com.braydenoneal.data.controller.terminal.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.TerminalType;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;

public record BlockStateTerminal(BlockState value) implements Terminal {
    public static final MapCodec<BlockStateTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockState.CODEC.fieldOf("value").forGetter(BlockStateTerminal::value)
    ).apply(instance, BlockStateTerminal::new));

    public static BlockState getValue(Context context, Either<Terminal, Function> input) throws Exception {
        if (Terminal.getTerminal(context, input) instanceof BlockStateTerminal(BlockState value)) {
            return value;
        }

        throw new Exception("Value is not a block state");
    }

    @Override
    public String getName() {
        return "Block State";
    }

    @Override
    public String getValueAsString() {
        return value.toString();
    }

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.BLOCK_STATE_TERMINAL;
    }
}
