package com.braydenoneal

import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.ValueTypes
import com.braydenoneal.blang.parser.statement.StatementTypes
import com.braydenoneal.block.ModBlocks
import com.braydenoneal.block.entity.ModBlockEntities
import com.braydenoneal.item.ModItems
import com.braydenoneal.networking.ModNetworking
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Blogic : ModInitializer {
    override fun onInitialize() {
        ModBlocks.initialize()
        ModBlockEntities.initialize()
        ModItems.initialize()
        ModNetworking.initialize()
        ValueTypes.initialize()
        ExpressionTypes.initialize()
        StatementTypes.initialize()
    }

    companion object {
        const val MOD_ID = "blogic"
        val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    }
}
