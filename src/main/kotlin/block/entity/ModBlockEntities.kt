package block.entity

import Blogic
import block.ModBlocks
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class ModBlockEntities {
    companion object {
        val CONTROLLER_BLOCK_ENTITY = register("controller", FabricBlockEntityTypeBuilder.Factory { pos: BlockPos, state: BlockState -> ControllerBlockEntity(pos, state) }, ModBlocks.CONTROLLER)

        val CONTROLLER_SCREEN_HANDLER: ExtendedMenuType<ControllerScreenHandler, BlockPos> = Registry.register(
            BuiltInRegistries.MENU,
            Identifier.fromNamespaceAndPath("blogic", "controller_block"),
            ExtendedMenuType(
                { syncId: Int, playerInventory: Inventory, pos: BlockPos ->
                    ControllerScreenHandler(syncId, playerInventory, pos)
                },
                BlockPos.STREAM_CODEC,
            ),
        )

        private fun <T : BlockEntity> register(
            name: String,
            entityFactory: FabricBlockEntityTypeBuilder.Factory<out T>,
            vararg blocks: Block,
        ): BlockEntityType<T> {
            val id = Identifier.fromNamespaceAndPath(Blogic.MOD_ID, name)
            return Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE, id,
                FabricBlockEntityTypeBuilder.create<T>(entityFactory, *blocks).build(),
            )
        }

        fun initialize() {
        }
    }
}
