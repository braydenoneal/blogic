package com.braydenoneal;

import com.braydenoneal.block.ModBlocks;
import com.braydenoneal.block.entity.ModBlockEntities;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Blogic implements ModInitializer {
    public static final String MOD_ID = "blogic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.initialize();
        ModBlockEntities.initialize();
    }
}
