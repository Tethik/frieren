package com.tethik.frieren.item;

import com.tethik.frieren.ModItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EndlessLavaBucket extends BucketItem {
    public EndlessLavaBucket() {
        super(Fluids.LAVA, new EndlessLavaBucketSettings());
        this.recipeRemainder = this; // Hacked via accesswidener - normally not accessible.

        DispenserBlock.registerBehavior(this, EndlessLavaBucketDispenserBehaviour);
    }

    public static DispenserBehavior EndlessLavaBucketDispenserBehaviour = new ItemDispenserBehavior() {
        private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            FluidModificationItem fluidModificationItem = (FluidModificationItem)stack.getItem();
            BlockPos blockPos = pointer.pos().offset((Direction)pointer.state().get(DispenserBlock.FACING));
            World world = pointer.world();
            if (fluidModificationItem.placeFluid((PlayerEntity)null, world, blockPos, (BlockHitResult)null)) {
                fluidModificationItem.onEmptied((PlayerEntity)null, world, stack, blockPos);
                return this.decrementStackWithRemainder(pointer, stack, new ItemStack(ModItems.ENDLESS_LAVA_BUCKET));
            } else {
                return this.fallbackBehavior.dispense(pointer, stack);
            }
        }
    };


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("itemTooltip.frieren.endless_lava_bucket").formatted(Formatting.GOLD));
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
