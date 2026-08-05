package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import program.Program
import program.expression.Arguments
import program.expression.value.*
import program.expression.value.Static

class BlockValue(value: Block) : Value<Block>(value) {
    override fun typeString(): String = "block"

    override fun equals(other: Any?): Boolean {
        return other is BlockValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.BLOCK_VALUE_CODEC.hashCode()
    }

    context(program: Program, arguments: Arguments)
    override fun innerCallFunction(name: String, local: Boolean): Value<*> {
        return when (name) {
            "asItem" -> asItem()
            "tags" -> tags()
            else -> super.innerCallFunction(name, local)
        }
    }

    fun asItem(): Value<*> {
        return ItemValue(value.asItem())
    }

    fun tags(): Value<*> {
        return ListValue(value.defaultBlockState().tags().map { BlockTagValue(it) }.toList().toMutableList())
    }

    companion object : Static {
        override val name: String = "Block"

        context(program: Program, arguments: Arguments)
        override fun innerCall(): Value<*> {
            val name = get<StringValue>("name").value
            return BlockValue(BuiltInRegistries.BLOCK.getValue(Identifier.parse(name)))
        }
    }
}
