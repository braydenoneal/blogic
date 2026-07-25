import blang.codec.expression.ExpressionType
import blang.codec.statement.StatementType
import blang.codec.value.ValueType
import blang.expression.BuiltinFunctions
import block.ModBlocks
import block.entity.ModBlockEntities
import item.ModItems
import net.fabricmc.api.ModInitializer
import networking.ModNetworking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import program.Program

object Blogic : ModInitializer {
    const val MOD_ID = "blogic"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        Program.initialize()
        BuiltinFunctions.initialize()
        ValueType.initialize()
        ExpressionType.initialize()
        StatementType.initialize()
        ModBlocks.initialize()
        ModBlockEntities.initialize()
        ModItems.initialize()
        ModNetworking.initialize()
    }
}
