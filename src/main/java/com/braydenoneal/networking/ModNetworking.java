package com.braydenoneal.networking;

import com.braydenoneal.block.entity.ControllerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.Objects;

public class ModNetworking {
    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(StringPayload.ID, StringPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(StringPayload.ID, (stringPayload, context) -> context.server().execute(() -> ((ControllerBlockEntity) Objects.requireNonNull(context.player().getWorld().getBlockEntity(stringPayload.pos()))).setSource(stringPayload.string())));
    }
}
