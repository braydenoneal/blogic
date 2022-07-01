package com.braydenoneal.blogicmod.item;

import com.braydenoneal.blogicmod.BlogicMod;
import com.braydenoneal.blogicmod.block.ModBlocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
	public static final ItemGroup BLOGIC = FabricItemGroupBuilder.build(
			new Identifier(BlogicMod.MOD_ID, "blogic"), () -> new ItemStack(ModBlocks.DIODE.asItem()));
}
