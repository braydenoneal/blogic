package blang.expression.value.block

import blang.codec.value.ValueCodecs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import program.Program
import program.expression.Arguments
import program.expression.value.Static
import program.expression.value.Value
import program.expression.value.get
import program.expression.value.string.StringValue

class BlockValue(value: Block) : Value<Block>(value) {
    override fun equals(other: Any?): Boolean {
        return other is BlockValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.BLOCK_VALUE_CODEC.hashCode()
    }

    context(program: Program)
    override fun getItem(name: String): Value<*> {
        return static.items[name]?.get() ?: super.getItem(name)
    }

    context(program: Program, arguments: Arguments)
    override fun innerCallFunction(name: String, local: Boolean): Value<*> {
        return static.functions[name]?.call() ?: super.innerCallFunction(name, local)
    }

    override val static = Companion

    companion object : Static<BlockValue>() {
        override val name = "Block"

        context(program: Program, arguments: Arguments)
        override fun innerCall(): Value<*> {
            val name = get<StringValue>("name").value
            return BlockValue(BuiltInRegistries.BLOCK.getValue(Identifier.parse(name)))
        }

        override fun initializeFunctions() {
            register(AsItemFunction)
            register(TagsFunction)
        }
    }
}
