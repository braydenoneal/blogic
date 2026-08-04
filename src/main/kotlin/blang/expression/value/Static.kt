package blang.expression.value

import program.expression.value.Static

object Static {
    fun initialize() {
        Static.register(BlockValue.Companion)
        Static.register(ItemValue.Companion)
        Static.register(ItemTagValue.Companion)
        Static.register(BlockTagValue.Companion)
    }
}
