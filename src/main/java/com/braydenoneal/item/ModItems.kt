package com.braydenoneal.item

import com.braydenoneal.Blogic
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.function.Function

object ModItems {
    fun register(name: String, itemFactory: Function<Item.Settings, Item>, settings: Item.Settings): Item {
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Blogic.MOD_ID, name))
        val item = itemFactory.apply(settings.registryKey(itemKey))
        Registry.register(Registries.ITEM, itemKey, item)
        return item
    }

    fun initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(
            ModifyEntries { itemGroup: FabricItemGroupEntries -> }
        )
    }
}
