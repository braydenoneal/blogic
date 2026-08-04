package blang.expression

import blang.expression.builtin.*
import program.expression.BuiltinFunctions.register

object BuiltinFunctions {
    fun initialize() {
        register("print", PrintBuiltin::call)
        register("breakBlock", BreakBlockBuiltin::call)
        register("deleteItems", DeleteItemsBuiltin::call)
        register("exportAllItems", ExportAllItemsBuiltin::call)
        register("getBlock", GetBlockBuiltin::call)
        register("getItemCount", GetItemCountBuiltin::call)
        register("getItems", GetItemsBuiltin::call)
        register("placeBlock", PlaceBlockBuiltin::call)
        register("readItemCount", ReadItemCountBuiltin::call)
        register("useItem", UseItemBuiltin::call)
    }
}
