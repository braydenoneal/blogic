package blang.expression.value

import blang.expression.value.block.BlockValue
import blang.expression.value.blockpos.BlockPosValue
import blang.expression.value.blocktag.BlockTagValue
import blang.expression.value.item.ItemValue
import blang.expression.value.itemstack.ItemStackValue
import blang.expression.value.itemtag.ItemTagValue
import program.expression.value.Static.Companion.register

object BlogicStatic {
    fun initialize() {
        register(BlockValue.Companion)
        register(BlockPosValue.Companion)
        register(BlockTagValue.Companion)
        register(ItemValue.Companion)
        register(ItemStackValue.Companion)
        register(ItemTagValue.Companion)
    }
}
