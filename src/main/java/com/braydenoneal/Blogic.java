package com.braydenoneal;

import com.braydenoneal.block.ModBlocks;
import com.braydenoneal.block.entity.ModBlockEntities;
import com.braydenoneal.component.ModComponents;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.parameter.ParameterTypes;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.braydenoneal.item.ModItems;
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
        ModItems.initialize();
        ModComponents.initialize();
        FunctionTypes.initialize();
        ParameterTypes.initialize();
        TerminalTypes.initialize();
    }
}
