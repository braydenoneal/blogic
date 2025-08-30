package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BlockValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public record GetBlockBuiltin(List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> xValue = arguments.get(0).evaluate(program);
        Value<?> yValue = arguments.get(1).evaluate(program);
        Value<?> zValue = arguments.get(2).evaluate(program);

        if (xValue instanceof IntegerValue x && yValue instanceof IntegerValue y && zValue instanceof IntegerValue z) {
            BlockPos entityPos = program.context().pos();
            BlockPos pos = new BlockPos(entityPos.getX() + x.value(), entityPos.getY() + y.value(), entityPos.getZ() + z.value());
            World world = program.context().entity().getWorld();

            if (world != null) {
                return new BlockValue(world.getBlockState(pos).getBlock());
            }
        }

        throw new RunException("Invalid arguments");
    }

    public static final MapCodec<GetBlockBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(GetBlockBuiltin::arguments)
    ).apply(instance, GetBlockBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.GET_BLOCK_BUILTIN;
    }
}
