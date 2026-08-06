package blang.expression.value.blocktag

import blang.codec.value.ValueCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import program.Program
import program.expression.Arguments
import program.expression.value.Static
import program.expression.value.Value
import program.expression.value.get
import program.expression.value.string.StringValue

class BlockTagValue(value: TagKey<Block>) : Value<TagKey<Block>>(value) {

    override fun equals(other: Any?): Boolean {
        return other is BlockTagValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.BLOCK_TAG_CODEC.hashCode()
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

    companion object : Static<BlockTagValue>() {
        override val name = "BlockTag"

        context(program: Program, arguments: Arguments)
        override fun innerCall(): Value<*> {
            val name = get<StringValue>("name").value
            return BlockTagValue(TagKey.create(Registries.BLOCK, Identifier.parse(name)))
        }
    }
}
