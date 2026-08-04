package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import program.Program
import program.expression.Arguments
import program.expression.value.Static
import program.expression.value.StringValue
import program.expression.value.Value

class BlockTagValue(value: TagKey<Block>) : Value<TagKey<Block>>(value) {
    override fun typeString(): String = "blockTag"

    override fun equals(other: Any?): Boolean {
        return other is BlockTagValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.BLOCK_TAG_CODEC.hashCode()
    }

    companion object : Static {
        override val name: String = "BlockTag"

        override fun innerCall(program: Program, arguments: Arguments): Value<*> {
            val name = arguments.get<StringValue>(program, "name").value
            return BlockTagValue(TagKey.create(Registries.BLOCK, Identifier.parse(name)))
        }
    }
}
