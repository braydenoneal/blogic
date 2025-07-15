package com.braydenoneal.block.entity;

import com.braydenoneal.Blogic;
import com.braydenoneal.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<RedstoneReaderBlockEntity> REDSTONE_READER_BLOCK_ENTITY =
            register("redstone_reader", RedstoneReaderBlockEntity::new, ModBlocks.REDSTONE_READER);
    public static final BlockEntityType<RedstoneWriterBlockEntity> REDSTONE_WRITER_BLOCK_ENTITY =
            register("redstone_reader", RedstoneWriterBlockEntity::new, ModBlocks.REDSTONE_WRITER);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(Blogic.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void initialize() {
    }
}
