package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BlockValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public record GetBlockBuiltin(Program program, List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> xValue = arguments.get(0).evaluate();
        Value<?> yValue = arguments.get(1).evaluate();
        Value<?> zValue = arguments.get(2).evaluate();

        if (xValue instanceof IntegerValue x && yValue instanceof IntegerValue y && zValue instanceof IntegerValue z) {
            BlockPos entityPos = program.context().pos();
            BlockPos pos = new BlockPos(entityPos.getX() + x.value(), entityPos.getY() + y.value(), entityPos.getZ() + z.value());
            return new BlockValue(program.context().world().getBlockState(pos).getBlock());
        }

        System.out.println("getBlock");
        System.out.println(xValue);
        System.out.println(yValue);
        System.out.println(zValue);
        return null;
    }
}
