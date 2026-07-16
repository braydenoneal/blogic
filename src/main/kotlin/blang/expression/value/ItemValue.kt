package blang.expression.value

import blang.codec.value.ValueTypes
import net.minecraft.world.item.Item
import program.expression.value.Value

class ItemValue(value: Item) : Value<Item>(value) {
    override fun equals(other: Any?): Boolean {
        if (other is ItemValue) {
            return value == other.value
        }

        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ValueTypes.ITEM.hashCode()
        return result
    }
}
