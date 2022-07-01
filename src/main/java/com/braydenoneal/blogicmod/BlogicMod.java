package com.braydenoneal.blogicmod;

import com.braydenoneal.blogicmod.block.ModBlocks;
import com.braydenoneal.blogicmod.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogicMod implements ModInitializer {
	public static final String MOD_ID = "blogicmod";
	public static final Logger LOGGER = LoggerFactory.getLogger("Blogic");

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		LOGGER.info("Hello Fabric world!");
	}
}
