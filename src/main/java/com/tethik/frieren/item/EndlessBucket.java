package com.tethik.frieren.item;

import com.tethik.frieren.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

public class EndlessBucket extends BucketItem {

    public Fluid fluidRef;

    public EndlessBucket(Fluid fluid, Settings settings) {
        super(fluid, settings);
        this.recipeRemainder = this; // Hacked via accesswidener - normally not accessible.
        this.fluidRef = fluid; // Used in behaviours to check specific actions. BucketItem has it too but not accessible.
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("itemTooltip.frieren.endless_bucket").formatted(Formatting.GOLD));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        // Reuse existing bucket logic
        ActionResult result = super.use(world, user, hand);
        if (!result.isAccepted()) {
            return result;
        }

        // Don't return empty bucket, just the same item.
        ItemStack itemStack = user.getStackInHand(hand);
        ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, user, new ItemStack(ModItems.ENDLESS_LAVA_BUCKET));
        return ActionResult.SUCCESS.withNewHandStack(itemStack2);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
