import blang.codec.expression.ExpressionTypes
import blang.codec.statement.StatementTypes
import blang.codec.value.ValueTypes
import block.ModBlocks
import block.entity.ModBlockEntities
import item.ModItems
import net.fabricmc.api.ModInitializer
import networking.ModNetworking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import parser.Parsers

object Blogic : ModInitializer {
    const val MOD_ID = "blogic"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        ValueTypes.initialize()
        ExpressionTypes.initialize()
        StatementTypes.initialize()
        Parsers.initialize()
        ModBlocks.initialize()
        ModBlockEntities.initialize()
        ModItems.initialize()
        ModNetworking.initialize()
    }
}
