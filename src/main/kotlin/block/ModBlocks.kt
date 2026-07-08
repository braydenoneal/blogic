package block

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Function

object ModBlocks {
    val CABLE: Block = register("cable", { CableBlock(it) }, CableBlock.settings())
    val CONTROLLER: Block = register("controller", { ControllerBlock(it) }, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE))

    private fun register(name: String, blockFactory: Function<BlockBehaviour.Properties, Block>, settings: BlockBehaviour.Properties): Block {
        val blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Blogic.MOD_ID, name))
        val block = blockFactory.apply(settings.setId(blockKey))
        val itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Blogic.MOD_ID, name))
        val blockItem = BlockItem(block, Item.Properties().setId(itemKey))
        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem)
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block)
    }

    fun initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(
            ModifyEntries {
                it.addBefore(Items.REDSTONE, CABLE.asItem())
                it.addBefore(Items.REDSTONE, CONTROLLER.asItem())
            },
        )
    }
}
