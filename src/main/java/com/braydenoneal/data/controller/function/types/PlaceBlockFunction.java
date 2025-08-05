package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class PlaceBlockFunction implements Function {
    public static final MapCodec<PlaceBlockFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("x").forGetter(PlaceBlockFunction::x),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("y").forGetter(PlaceBlockFunction::y),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("z").forGetter(PlaceBlockFunction::z),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("block").forGetter(PlaceBlockFunction::block),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("predicate").forGetter(PlaceBlockFunction::predicate)
            ).apply(instance, PlaceBlockFunction::new)
    );

    private Either<Terminal, Function> x;
    private Either<Terminal, Function> y;
    private Either<Terminal, Function> z;
    private Either<Terminal, Function> block;
    private Either<Terminal, Function> predicate;

    public PlaceBlockFunction(Either<Terminal, Function> x, Either<Terminal, Function> y, Either<Terminal, Function> z, Either<Terminal, Function> block, Either<Terminal, Function> predicate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.predicate = predicate;
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

    public Either<Terminal, Function> block() {
        return block;
    }

    public Either<Terminal, Function> predicate() {
        return predicate;
    }

    @Override
    public Terminal method(Context context) throws Exception {
        int xValue = IntegerTerminal.getValue(context, x);
        int yValue = IntegerTerminal.getValue(context, y);
        int zValue = IntegerTerminal.getValue(context, z);
        int blockValue = IntegerTerminal.getValue(context, block);
        boolean predicateValue = BooleanTerminal.getValue(context, predicate);

        if (predicateValue) {
            BlockPos readPos = context.pos().add(xValue, yValue, zValue);
            context.world().setBlockState(readPos, blockValue == 0 ? Blocks.AIR.getDefaultState() : Blocks.STONE.getDefaultState());
        }

        return new VoidTerminal();
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("place block"),
                new LabelGuiComponent("x"),
                new ParameterGuiComponent("x", x),
                new LabelGuiComponent("y"),
                new ParameterGuiComponent("y", y),
                new LabelGuiComponent("z"),
                new ParameterGuiComponent("z", z),
                new LabelGuiComponent("block"),
                new ParameterGuiComponent("block", block),
                new LabelGuiComponent("if"),
                new ParameterGuiComponent("predicate", predicate)
        );
    }

    @Override
    public Function withParam(String name, Either<Terminal, Function> value) {
        return switch (name) {
            case "x" -> new PlaceBlockFunction(value, y, z, block, predicate);
            case "y" -> new PlaceBlockFunction(x, value, z, block, predicate);
            case "z" -> new PlaceBlockFunction(x, y, value, block, predicate);
            case "block" -> new PlaceBlockFunction(x, y, z, value, predicate);
            case "predicate" -> new PlaceBlockFunction(x, y, z, block, value);
            default -> this;
        };
    }

    @Override
    public void setParameter(String name, Either<Terminal, Function> value) {
        switch (name) {
            case "x" -> x = value;
            case "y" -> y = value;
            case "z" -> z = value;
            case "block" -> block = value;
            case "predicate" -> predicate = value;
        }
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.PLACE_BLOCK_FUNCTION;
    }
}
