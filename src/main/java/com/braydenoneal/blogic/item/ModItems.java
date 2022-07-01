package com.braydenoneal.blogic.item;

import com.braydenoneal.blogic.BlogicMod;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
	private static Item registerItem(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(BlogicMod.MOD_ID, name), item);
	}

	public static void registerModItems() {}
}
