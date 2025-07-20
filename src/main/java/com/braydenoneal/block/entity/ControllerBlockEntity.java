package com.braydenoneal.block.entity;

import com.braydenoneal.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ControllerBlockEntity extends AbstractNetworkBlockEntity implements SingleStackInventory.SingleStackBlockEntityInventory {
    private String name;
    private ItemStack fileStack;

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state);
        name = pos.toShortString();
        fileStack = ItemStack.EMPTY;
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        name = view.getString("name", "");
        fileStack = view.read("Files", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putString("name", name);
        view.put("Files", ItemStack.CODEC, getStack());
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state) {
    }

    @Override
    public BlockEntity asBlockEntity() {
        return this;
    }

    @Override
    public ItemStack getStack() {
        return fileStack;
    }

    @Override
    public void setStack(ItemStack stack) {
        fileStack = stack;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return stack.isOf(ModItems.FILE);
    }
}
