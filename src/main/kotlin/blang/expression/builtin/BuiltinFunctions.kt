package blang.expression.builtin

import program.expression.builtin.BuiltinFunctions

object BuiltinFunctions {
    fun initialize() {
        BuiltinFunctions.register("print", PrintBuiltin)
        BuiltinFunctions.register("breakBlock", BreakBlockBuiltin)
        BuiltinFunctions.register("deleteItems", DeleteItemsBuiltin)
        BuiltinFunctions.register("exportAllItems", ExportAllItemsBuiltin)
        BuiltinFunctions.register("getBlock", GetBlockBuiltin)
        BuiltinFunctions.register("getBlockPos", GetBlockPosBuiltin)
        BuiltinFunctions.register("getItemCount", GetItemCountBuiltin)
        BuiltinFunctions.register("getItems", GetItemsBuiltin)
        BuiltinFunctions.register("placeBlock", PlaceBlockBuiltin)
        BuiltinFunctions.register("readItemCount", ReadItemCountBuiltin)
        BuiltinFunctions.register("useItem", UseItemBuiltin)
    }
}
