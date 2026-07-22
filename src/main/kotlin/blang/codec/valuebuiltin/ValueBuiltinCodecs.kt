package blang.codec.valuebuiltin

import blang.codec.expression.ExpressionCodecs.ARGUMENTS_CODEC
import blang.codec.value.ValueCodecs
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import program.expression.builtin.list.*
import program.expression.builtin.struct.StructEntriesBuiltin
import program.expression.builtin.struct.StructKeysBuiltin
import program.expression.builtin.struct.StructRemoveBuiltin
import program.expression.builtin.struct.StructValuesBuiltin

object ValueBuiltinCodecs {
    val LIST_APPEND_BUILTIN_CODEC: MapCodec<ListAppendBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListAppendBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListAppendBuiltin::arguments),
        ).apply(it, ::ListAppendBuiltin)
    }
    val LIST_CONTAINS_ALL_BUILTIN_CODEC: MapCodec<ListContainsAllBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListContainsAllBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListContainsAllBuiltin::arguments),
        ).apply(it, ::ListContainsAllBuiltin)
    }
    val LIST_CONTAINS_BUILTIN_CODEC: MapCodec<ListContainsBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListContainsBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListContainsBuiltin::arguments),
        ).apply(it, ::ListContainsBuiltin)
    }
    val LIST_INSERT_BUILTIN_CODEC: MapCodec<ListInsertBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListInsertBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListInsertBuiltin::arguments),
        ).apply(it, ::ListInsertBuiltin)
    }
    val LIST_POP_BUILTIN_CODEC: MapCodec<ListPopBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListPopBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListPopBuiltin::arguments),
        ).apply(it, ::ListPopBuiltin)
    }
    val LIST_REMOVE_BUILTIN_CODEC: MapCodec<ListRemoveBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListRemoveBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListRemoveBuiltin::arguments),
        ).apply(it, ::ListRemoveBuiltin)
    }
    val STRUCT_ENTRIES_BUILTIN_CODEC: MapCodec<StructEntriesBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("value").forGetter(StructEntriesBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructEntriesBuiltin::arguments),
        ).apply(it, ::StructEntriesBuiltin)
    }
    val STRUCT_KEYS_BUILTIN_CODEC: MapCodec<StructKeysBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("value").forGetter(StructKeysBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructKeysBuiltin::arguments),
        ).apply(it, ::StructKeysBuiltin)
    }
    val STRUCT_REMOVE_BUILTIN_CODEC: MapCodec<StructRemoveBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("value").forGetter(StructRemoveBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructRemoveBuiltin::arguments),
        ).apply(it, ::StructRemoveBuiltin)
    }
    val STRUCT_VALUES_BUILTIN_CODEC: MapCodec<StructValuesBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("value").forGetter(StructValuesBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructValuesBuiltin::arguments),
        ).apply(it, ::StructValuesBuiltin)
    }
}
