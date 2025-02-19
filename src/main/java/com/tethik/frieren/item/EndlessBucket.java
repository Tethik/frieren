package com.tethik.frieren.item;

import com.tethik.frieren.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
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
    public EndlessBucket(Fluid fluid, Settings settings) {
        super(fluid, settings);
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
            }

            return this.fallbackBehavior.dispense(pointer, stack);
        }
    };

    public static void registerBucketBehavior() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_LAVA_BUCKET, EndlessBucket::tryFillWithLava);
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_LAVA_BUCKET, EndlessBucket::tryFillWithLava);
        CauldronBehavior.LAVA_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_LAVA_BUCKET, EndlessBucket::tryFillWithLava);
        CauldronBehavior.POWDER_SNOW_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_LAVA_BUCKET, EndlessBucket::tryFillWithLava);

        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_WATER_BUCKET, EndlessBucket::tryFillWithWater);
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_WATER_BUCKET, EndlessBucket::tryFillWithWater);
        CauldronBehavior.LAVA_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_WATER_BUCKET, EndlessBucket::tryFillWithWater);
        CauldronBehavior.POWDER_SNOW_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_WATER_BUCKET, EndlessBucket::tryFillWithWater);
    }

    private static boolean isUnderwater(World world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos.up());
        return fluidState.isIn(FluidTags.WATER);
    }

    static ActionResult fillCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
        if (!world.isClient) {
            Item item = stack.getItem();
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, state);
            world.playSound((PlayerEntity)null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent((Entity)null, GameEvent.FLUID_PLACE, pos);
        }

        return ActionResult.SUCCESS;
    }

    private static ActionResult tryFillWithWater(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        return fillCauldron(world, pos, player, hand, stack, (BlockState)Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3), SoundEvents.ITEM_BUCKET_EMPTY);
    }

    private static ActionResult tryFillWithLava(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        return (ActionResult)(isUnderwater(world, pos) ? ActionResult.PASS : fillCauldron(world, pos, player, hand, stack, Blocks.LAVA_CAULDRON.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY_LAVA));
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
