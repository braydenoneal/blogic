package blang.expression.value

import net.minecraft.item.Item
import parser.RunException
import parser.expression.value.Value

class ItemValue(value: Item) : Value<Item>(value) {
    override fun equals(other: Any?): Boolean {
        try {
            if (other is ItemValue) {
                return value == other.value
            }
        } catch (_: Error) {
            throw RunException("Cannot equate item values outside of the game")
        }

        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + blang.codec.value.ValueTypes.ITEM.hashCode()
        return result
    }
}
