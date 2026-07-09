package blang.expression.value

import blang.codec.value.ValueTypes
import net.minecraft.world.item.ItemStack
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
        result = 31 * result + ValueTypes.ITEM_STACK.hashCode()
        return result
    }
}
