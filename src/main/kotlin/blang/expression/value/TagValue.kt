package blang.expression.value

import blang.codec.value.ValueTypes
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import program.expression.value.Value

class TagValue(value: TagKey<Item>) : Value<TagKey<Item>>(value) {
    override fun equals(other: Any?): Boolean {
        if (other is TagValue) {
            return value == other.value
        }

        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ValueTypes.TAG.hashCode()
        return result
    }
}
