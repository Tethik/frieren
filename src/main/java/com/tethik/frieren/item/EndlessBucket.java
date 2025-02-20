package com.tethik.frieren.item;

import com.tethik.frieren.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

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
