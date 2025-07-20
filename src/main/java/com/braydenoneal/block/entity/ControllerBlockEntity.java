package com.braydenoneal.block.entity;

import com.braydenoneal.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ControllerBlockEntity extends AbstractNetworkBlockEntity implements Inventory {
    private String name;
    protected DefaultedList<ItemStack> inventory;

    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTROLLER_BLOCK_ENTITY, pos, state);
        name = pos.toShortString();
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        name = view.getString("name", "");
        inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
        Inventories.readData(view, inventory);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putString("name", name);
        Inventories.writeData(view, inventory);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void update(World world, BlockPos pos, BlockState state) {
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : inventory) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < inventory.size() ? inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(inventory, slot, amount);

        if (!itemStack.isEmpty()) {
            this.markDirty();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = inventory.get(slot);

        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            inventory.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return stack.isOf(ModItems.FILE);
    }

    @Override
    public void clear() {
        inventory.clear();
        this.markDirty();
    }
}
