package com.braydenoneal.networking

import com.braydenoneal.block.entity.ControllerBlockEntity
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object ModNetworking {
    fun initialize() {
        PayloadTypeRegistry.playC2S().register(StringPayload.ID, StringPayload.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(StringPayload.ID)
        { stringPayload: StringPayload, context: ServerPlayNetworking.Context ->
            context.server().execute {
                (context.player().world.getBlockEntity(stringPayload.pos) as ControllerBlockEntity).setSource(stringPayload.string)
            }
        }
    }
}
