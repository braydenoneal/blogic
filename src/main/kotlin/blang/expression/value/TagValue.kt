package blang.expression.value

import net.minecraft.item.Item
import net.minecraft.registry.tag.TagKey
import parser.RunException
import parser.expression.value.Value

class TagValue(value: TagKey<Item>) : Value<TagKey<Item>>(value) {
    override fun equals(other: Any?): Boolean {
        try {
            if (other is TagValue) {
                return value == other.value
            }
        } catch (_: Error) {
            throw RunException("Cannot equate tag values outside of the game")
        }

        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + blang.codec.value.ValueTypes.TAG.hashCode()
        return result
    }
}
