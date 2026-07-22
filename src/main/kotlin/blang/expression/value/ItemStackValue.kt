package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.world.item.ItemStack
import program.expression.value.Value

class ItemStackValue(value: ItemStack) : Value<ItemStack>(value) {
    override fun typeString(): String = "itemStack"

    override fun equals(other: Any?): Boolean {
        return other is ItemStackValue && value == other.value
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ValueCodecs.ITEM_STACK_CODEC.hashCode()
        return result
    }
}
