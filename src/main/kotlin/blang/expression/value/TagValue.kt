package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import program.expression.value.Value

class TagValue(value: TagKey<Item>) : Value<TagKey<Item>>(value) {
    override fun typeString(): String = "tag"

    override fun equals(other: Any?): Boolean {
        return other is TagValue && value == other.value
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ValueCodecs.TAG_CODEC.hashCode()
        return result
    }
}
