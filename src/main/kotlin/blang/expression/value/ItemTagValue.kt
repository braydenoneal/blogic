package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import program.Program
import program.expression.Arguments
import program.expression.value.Static
import program.expression.value.StringValue
import program.expression.value.Value
import program.expression.value.get

class ItemTagValue(value: TagKey<Item>) : Value<TagKey<Item>>(value) {
    override fun typeString(): String = "itemTag"

    override fun equals(other: Any?): Boolean {
        return other is ItemTagValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.ITEM_TAG_CODEC.hashCode()
    }

    companion object : Static {
        override val name: String = "ItemTag"

        context(program: Program, arguments: Arguments)
        override fun innerCall(): Value<*> {
            val name = get<StringValue>("name").value
            return ItemTagValue(TagKey.create(Registries.ITEM, Identifier.parse(name)))
        }
    }
}
