package blang.expression.value.itemstack

import blang.codec.value.ValueCodecs
import net.minecraft.world.item.ItemStack
import program.Program
import program.expression.Arguments
import program.expression.value.Static
import program.expression.value.Value

class ItemStackValue(value: ItemStack) : Value<ItemStack>(value) {
    override fun equals(other: Any?): Boolean {
        return other is ItemStackValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.ITEM_STACK_CODEC.hashCode()
    }

    context(program: Program)
    override fun getItem(name: String): Value<*> {
        return static.items[name]?.get() ?: super.getItem(name)
    }

    context(program: Program, arguments: Arguments)
    override fun innerCallFunction(name: String, local: Boolean): Value<*> {
        return static.functions[name]?.call() ?: super.innerCallFunction(name, local)
    }

    override val static = Companion

    companion object : Static<ItemStackValue>() {
        override val name = "ItemStack"
    }
}
