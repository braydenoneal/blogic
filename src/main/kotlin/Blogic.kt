import blang.codec.builtin.BuiltinType
import blang.codec.expression.ExpressionType
import blang.codec.statement.StatementType
import blang.codec.value.ValueType
import blang.codec.valuebuiltin.ValueBuiltinType
import block.ModBlocks
import block.entity.ModBlockEntities
import item.ModItems
import net.fabricmc.api.ModInitializer
import networking.ModNetworking
import program.Program

object Blogic : ModInitializer {
    const val MOD_ID = "blogic"

    override fun onInitialize() {
        Program.initialize()
        ValueType.initialize()
        ValueBuiltinType.initialize()
        BuiltinType.initialize()
        ExpressionType.initialize()
        StatementType.initialize()
        ModBlocks.initialize()
        ModBlockEntities.initialize()
        ModItems.initialize()
        ModNetworking.initialize()
    }
}
