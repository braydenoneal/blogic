package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BlockValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record GetBlockBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        IntegerValue x = arguments.integerValue(program, "x");
        IntegerValue y = arguments.integerValue(program, "y");
        IntegerValue z = arguments.integerValue(program, "z");

        BlockPos entityPos = program.context().pos();
        BlockPos pos = new BlockPos(entityPos.getX() + x.value(), entityPos.getY() + y.value(), entityPos.getZ() + z.value());
        World world = program.context().entity().getWorld();

        if (world == null) {
            throw new RunException("World is null");
        }

        return new BlockValue(world.getBlockState(pos).getBlock());
    }

    public static final MapCodec<GetBlockBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(GetBlockBuiltin::arguments)
    ).apply(instance, GetBlockBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.GET_BLOCK_BUILTIN;
    }
}
