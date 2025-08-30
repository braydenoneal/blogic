package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.FunctionValue;
import com.braydenoneal.blang.parser.expression.value.ItemValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public record UseItemBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        int x = arguments.integerValue(program, "x", 0).value();
        int y = arguments.integerValue(program, "y", 1).value();
        int z = arguments.integerValue(program, "z", 2).value();
        FunctionValue itemPredicate = arguments.functionValue(program, "itemPredicate", 3);

        BlockPos entityPos = program.context().pos();
        BlockPos pos = new BlockPos(entityPos.getX() + x, entityPos.getY() + y, entityPos.getZ() + z);
        World world = program.context().entity().getWorld();

        if (world == null) {
            throw new RunException("World is null");
        }

        List<LockableContainerBlockEntity> containers = program.context().entity().getConnectedContainers();

        for (LockableContainerBlockEntity container : containers) {
            for (int slot = 0; slot < container.size(); slot++) {
                ItemStack stack = container.getStack(slot);

                if (stack.isOf(Items.AIR)) {
                    continue;
                }

                program.newScope();
                program.getScope().set(itemPredicate.value().arguments().getFirst(), new ItemValue(stack.getItem()));
                Value<?> predicateResult = itemPredicate.call(program);
                program.endScope();

                if (!(predicateResult instanceof BooleanValue)) {
                    throw new RunException("itemPredicate is not a predicate");
                }

                if (!((BooleanValue) predicateResult).value()) {
                    continue;
                }

                Direction facing = program.context().entity().getFacing();
                BlockHitResult hit = new BlockHitResult(pos.toCenterPos(), facing, pos, false);
                FakePlayer player = FakePlayer.get((ServerWorld) world);

                stack.useOnBlock(new ItemUsageContext(world, player, Hand.MAIN_HAND, stack, hit));

                container.setStack(slot, stack);
                return new BooleanValue(true);
            }
        }

        return new BooleanValue(false);
    }

    public static final MapCodec<UseItemBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(UseItemBuiltin::arguments)
    ).apply(instance, UseItemBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.USE_ITEM_BUILTIN;
    }
}
