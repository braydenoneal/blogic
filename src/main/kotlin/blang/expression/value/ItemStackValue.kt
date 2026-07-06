package blang.expression.value

import net.minecraft.item.ItemStack
import parser.RunException
import parser.expression.value.Value

class ItemStackValue(value: ItemStack) : Value<ItemStack>(value) {
    override fun equals(other: Any?): Boolean {
        try {
            if (other is ItemStackValue) {
                return value == other.value
            }
        } catch (_: Error) {
            throw RunException("Cannot equate item stack values outside of the game")
        }

        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + blang.codec.value.ValueTypes.ITEM_STACK.hashCode()
        return result
    }
}
