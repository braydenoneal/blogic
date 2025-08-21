package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public record PlaceBlockBuiltin(Program program, List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> xValue = arguments.get(0).evaluate();
        Value<?> yValue = arguments.get(1).evaluate();
        Value<?> zValue = arguments.get(2).evaluate();
        Value<?> blockValue = arguments.get(3).evaluate();

        if (xValue instanceof IntegerValue x &&
                yValue instanceof IntegerValue y &&
                zValue instanceof IntegerValue z &&
                blockValue instanceof StringValue block
        ) {
            BlockPos entityPos = program.context().pos();
            BlockPos pos = new BlockPos(entityPos.getX() + x.value(), entityPos.getY() + y.value(), entityPos.getZ() + z.value());
            program.context().world().setBlockState(pos, Registries.BLOCK.get(Identifier.of(block.value())).getDefaultState());
            return null;
        }

        System.out.println("placeBlock");
        System.out.println(xValue);
        System.out.println(yValue);
        System.out.println(zValue);
        System.out.println(blockValue);
        return null;
    }
}
