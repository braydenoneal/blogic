package com.braydenoneal.block.entity

import com.braydenoneal.Blogic
import com.braydenoneal.block.ModBlocks
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class ModBlockEntities {
    companion object {
        val CONTROLLER_BLOCK_ENTITY = register("controller", FabricBlockEntityTypeBuilder.Factory { pos: BlockPos, state: BlockState -> ControllerBlockEntity(pos, state) }, ModBlocks.CONTROLLER)

        val CONTROLLER_SCREEN_HANDLER: ExtendedScreenHandlerType<ControllerScreenHandler, BlockPos> =
            Registry.register<ScreenHandlerType<*>, ExtendedScreenHandlerType<ControllerScreenHandler, BlockPos>>(
                Registries.SCREEN_HANDLER,
                Identifier.of("blogic", "controller_block"),
                ExtendedScreenHandlerType<ControllerScreenHandler, BlockPos>({ syncId: Int, playerInventory: PlayerInventory, pos: BlockPos ->
                    ControllerScreenHandler(syncId, playerInventory, pos)
                }, BlockPos.PACKET_CODEC)
            )

        private fun <T : BlockEntity> register(
            name: String,
            entityFactory: FabricBlockEntityTypeBuilder.Factory<out T>,
            vararg blocks: Block
        ): BlockEntityType<T> {
            val id = Identifier.of(Blogic.MOD_ID, name)
            return Registry.register(
                Registries.BLOCK_ENTITY_TYPE, id,
                FabricBlockEntityTypeBuilder.create<T>(entityFactory, *blocks).build()
            )
        }

        fun initialize() {
        }
    }
}
