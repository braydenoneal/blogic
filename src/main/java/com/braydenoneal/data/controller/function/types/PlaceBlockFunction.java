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
import java.util.Map;

public record PlaceBlockFunction(
        Either<Terminal, Function> x,
        Either<Terminal, Function> y,
        Either<Terminal, Function> z,
        Either<Terminal, Function> block,
        Either<Terminal, Function> predicate
) implements Function {
    public static final MapCodec<PlaceBlockFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("x").forGetter(PlaceBlockFunction::x),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("y").forGetter(PlaceBlockFunction::y),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("z").forGetter(PlaceBlockFunction::z),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("block").forGetter(PlaceBlockFunction::block),
                    Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("predicate").forGetter(PlaceBlockFunction::predicate)
            ).apply(instance, PlaceBlockFunction::new)
    );

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
    public String getName() {
        return "Place Block";
    }

    @Override
    public Map<String, Either<Terminal, Function>> getParameters() {
        return Map.of("x", x, "y", y, "z", z, "block", block, "predicate", predicate);
    }

    @Override
    public List<GuiComponent> getGuiComponents() {
        return List.of(
                new LabelGuiComponent("place block"),
                new LabelGuiComponent("x"),
                new ParameterGuiComponent(x),
                new LabelGuiComponent("y"),
                new ParameterGuiComponent(y),
                new LabelGuiComponent("z"),
                new ParameterGuiComponent(z),
                new LabelGuiComponent("block"),
                new ParameterGuiComponent(block),
                new LabelGuiComponent("if"),
                new ParameterGuiComponent(predicate)
        );
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.PLACE_BLOCK_FUNCTION;
    }
}
