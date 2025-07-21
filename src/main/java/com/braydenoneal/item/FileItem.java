package com.braydenoneal.item;

import com.braydenoneal.Blogic;
import com.braydenoneal.component.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class FileItem extends Item {
    public FileItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        Blogic.LOGGER.info(stack.getOrDefault(ModComponents.NAME_COMPONENT, "EMPTY"));
        return ActionResult.SUCCESS;
    }
}
