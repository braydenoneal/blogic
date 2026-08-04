package blang.expression

import blang.expression.value.BlockTagValue
import blang.expression.value.BlockValue
import blang.expression.value.ItemTagValue
import blang.expression.value.ItemValue
import program.expression.value.Static.Companion.register

object Static {
    fun initialize() {
        register(BlockValue.Companion)
        register(ItemValue.Companion)
        register(ItemTagValue.Companion)
        register(BlockTagValue.Companion)
    }
}