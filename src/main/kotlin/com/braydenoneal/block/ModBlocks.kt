package com.braydenoneal.block

import com.braydenoneal.Blogic
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.function.Function

object ModBlocks {
    val CABLE: Block = register("cable", { CableBlock(it) }, CableBlock.settings())
    val CONTROLLER: Block = register("controller", { ControllerBlock(it) }, AbstractBlock.Settings.copy(Blocks.STONE))

    private fun register(name: String, blockFactory: Function<AbstractBlock.Settings, Block>, settings: AbstractBlock.Settings): Block {
        val blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Blogic.MOD_ID, name))
        val block = blockFactory.apply(settings.registryKey(blockKey))
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Blogic.MOD_ID, name))
        val blockItem = BlockItem(block, Item.Settings().registryKey(itemKey))
        Registry.register(Registries.ITEM, itemKey, blockItem)
        return Registry.register(Registries.BLOCK, blockKey, block)
    }

    fun initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(
            ModifyEntries {
                it.addBefore(Items.REDSTONE, CABLE.asItem())
                it.addBefore(Items.REDSTONE, CONTROLLER.asItem())
            }
        )
    }
}
