package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public record PrintBuiltin(Program program, Expression expression) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();
        String string = value.toString();

        if (value instanceof StringValue) {
            string = string.substring(1, string.length() - 1);
        }

        World world = program.context().entity().getWorld();

        if (world != null && world.getServer() != null) {
            for (ServerPlayerEntity player : world.getServer().getPlayerManager().getPlayerList()) {
                player.sendMessage(Text.of(string));
            }
        } else {
            System.out.println(string);
        }

        return null;
    }
}
