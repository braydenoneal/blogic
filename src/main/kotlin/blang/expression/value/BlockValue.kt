package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import program.Program
import program.expression.Arguments
import program.expression.value.ListValue
import program.expression.value.Static
import program.expression.value.StringValue
import program.expression.value.Value

class BlockValue(value: Block) : Value<Block>(value) {
    override fun typeString(): String = "block"

    override fun equals(other: Any?): Boolean {
        return other is BlockValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.BLOCK_VALUE_CODEC.hashCode()
    }

    override fun innerCallFunction(program: Program, arguments: Arguments, name: String, local: Boolean): Value<*> {
        return when (name) {
            "asItem" -> asItem()
            "tags" -> tags()
            else -> super.innerCallFunction(program, arguments, name, local)
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

        override fun innerCall(program: Program, arguments: Arguments): Value<*> {
            val name = arguments.get<StringValue>(program, "name").value
            return BlockValue(BuiltInRegistries.BLOCK.getValue(Identifier.parse(name)))
        }
    }
}
