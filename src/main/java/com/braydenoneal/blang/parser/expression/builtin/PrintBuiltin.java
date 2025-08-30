package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.Null;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public record PrintBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = arguments.arguments().isEmpty() ? new StringValue("") : arguments().anyValue(program, "value", 0);
        String string = value.toString();

        if (value instanceof StringValue) {
            string = string.substring(1, string.length() - 1);
        }

        if (program.context().entity() != null) {
            World world = program.context().entity().getWorld();

            if (world != null && world.getServer() != null) {
                for (ServerPlayerEntity player : world.getServer().getPlayerManager().getPlayerList()) {
                    player.sendMessage(Text.of(string));
                }
            }
        } else {
            System.out.println(string);
        }

        return Null.value();
    }

    public static final MapCodec<PrintBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(PrintBuiltin::arguments)
    ).apply(instance, PrintBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.PRINT_BUILTIN;
    }
}
