package blang.expression.value

import blang.codec.value.ValueTypes
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
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
        result = 31 * result + ValueTypes.TAG.hashCode()
        return result
    }
}
