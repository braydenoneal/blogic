package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ReadRedstoneFunction implements Function {
    public static final MapCodec<ReadRedstoneFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("x").forGetter(ReadRedstoneFunction::x),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("y").forGetter(ReadRedstoneFunction::y),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("z").forGetter(ReadRedstoneFunction::z)
            ).apply(instance, ReadRedstoneFunction::new)
    );

    private Either<Terminal, Function> x;
    private Either<Terminal, Function> y;
    private Either<Terminal, Function> z;

    public ReadRedstoneFunction(Either<Terminal, Function> x, Either<Terminal, Function> y, Either<Terminal, Function> z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Either<Terminal, Function> x() {
        return x;
    }

    public Either<Terminal, Function> y() {
        return y;
    }

    public Either<Terminal, Function> z() {
        return z;
    }

    @Override
    public Terminal method(Context context) throws Exception {
        int xValue = IntegerTerminal.getValue(context, x);
        int yValue = IntegerTerminal.getValue(context, y);
        int zValue = IntegerTerminal.getValue(context, z);

        BlockPos readPos = context.pos().add(xValue, yValue, zValue);
        // TODO: Handle auto-closable somehow? Is it possible for this variable to become out of date? Or is it fine
        //   since it is a reference?
        int redstoneValue = context.world().getReceivedRedstonePower(readPos);

        return new IntegerTerminal(redstoneValue);
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("read redstone"),
                new LabelGuiComponent("x"),
                new ParameterGuiComponent("x", x),
                new LabelGuiComponent("y"),
                new ParameterGuiComponent("y", y),
                new LabelGuiComponent("z"),
                new ParameterGuiComponent("z", z)
        );
    }

    @Override
    public Function withParam(String name, Either<Terminal, Function> value) {
        return switch (name) {
            case "x" -> new ReadRedstoneFunction(value, y, z);
            case "y" -> new ReadRedstoneFunction(x, value, z);
            case "z" -> new ReadRedstoneFunction(x, y, value);
            default -> this;
        };
    }

    @Override
    public void setParameter(String name, Either<Terminal, Function> value) {
        switch (name) {
            case "x" -> x = value;
            case "y" -> y = value;
            case "z" -> z = value;
        }
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.READ_REDSTONE_FUNCTION;
    }
}
