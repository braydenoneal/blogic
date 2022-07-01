package com.braydenoneal.blogic;

import com.braydenoneal.blogic.block.ModBlocks;
import com.braydenoneal.blogic.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogicMod implements ModInitializer {
	public static final String MOD_ID = "blogic";
	public static final Logger LOGGER = LoggerFactory.getLogger("Blogic");

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}
