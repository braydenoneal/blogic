package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import program.Program
import program.expression.Arguments
import program.expression.value.ListValue
import program.expression.value.Static
import program.expression.value.StringValue
import program.expression.value.Value

class ItemValue(value: Item) : Value<Item>(value) {
    override fun typeString(): String = "item"

    override fun equals(other: Any?): Boolean {
        return other is ItemValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.ITEM_STACK_CODEC.hashCode()
    }

    context(program: Program, arguments: Arguments)
    override fun innerCallFunction(name: String, local: Boolean): Value<*> {
        return when (name) {
            "tags" -> tags()
            else -> super.innerCallFunction(name, local)
        }
    }

    fun tags(): Value<*> {
        return ListValue(value.defaultInstance.tags().map { ItemTagValue(it) }.toList().toMutableList())
    }

    companion object : Static {
        override val name: String = "Item"

        context(program: Program, arguments: Arguments)
        override fun innerCall(): Value<*> {
            val name = arguments.get<StringValue>("name").value
            return ItemValue(BuiltInRegistries.ITEM.getValue(Identifier.parse(name)))
        }
    }
}
