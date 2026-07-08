package item

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import java.util.function.Function

object ModItems {
    fun register(name: String, itemFactory: Function<Item.Properties, Item>, settings: Item.Properties): Item {
        val itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Blogic.MOD_ID, name))
        val item = itemFactory.apply(settings.setId(itemKey))
        Registry.register(BuiltInRegistries.ITEM, itemKey, item)
        return item
    }

    fun initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(
            ModifyEntries {},
        )
    }
}
