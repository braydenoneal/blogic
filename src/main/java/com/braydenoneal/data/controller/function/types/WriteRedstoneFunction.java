package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.block.entity.ControllerBlockEntity;
import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;

public record WriteRedstoneFunction(Either<Terminal, Function> value) implements Function {
    public static final MapCodec<WriteRedstoneFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC)
                            .fieldOf("value").forGetter(WriteRedstoneFunction::value)
            ).apply(instance, WriteRedstoneFunction::new)
    );

    @Override
    public Terminal method(Context context) throws Exception {
        int valueValue = IntegerTerminal.getValue(context, value);
        BlockEntity blockEntity = context.world().getBlockEntity(context.pos());

        if (blockEntity instanceof ControllerBlockEntity controllerBlockEntity) {
            controllerBlockEntity.setEmitRedstoneValue(valueValue);
            context.world().setBlockState(context.pos().add(-2, 0, 0), Blocks.STONE.getDefaultState());
        }

        return new VoidTerminal();
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.WRITE_REDSTONE_FUNCTION;
    }
}
