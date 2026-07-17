package blang.expression.value

import blang.codec.value.ValueTypes
import net.minecraft.world.item.ItemStack
import program.expression.value.Value

class ItemStackValue(value: ItemStack) : Value<ItemStack>(value) {
    override fun typeString(): String = "itemStack"

    override fun equals(other: Any?): Boolean {
        return other is ItemStackValue && value == other.value
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ValueTypes.ITEM_STACK.hashCode()
        return result
    }
}
