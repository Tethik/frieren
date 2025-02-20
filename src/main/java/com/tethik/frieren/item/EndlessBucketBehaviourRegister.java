package com.tethik.frieren.item;

import com.tethik.frieren.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EndlessBucketBehaviourRegister {

    public static void registerDispenserBehavior() {
        DispenserBlock.registerBehavior(ModItems.ENDLESS_BUCKET, createDispenserBehaviour((EndlessBucket) ModItems.ENDLESS_BUCKET));
        DispenserBlock.registerBehavior(ModItems.ENDLESS_WATER_BUCKET, createDispenserBehaviour((EndlessBucket) ModItems.ENDLESS_WATER_BUCKET));
        DispenserBlock.registerBehavior(ModItems.ENDLESS_LAVA_BUCKET, createDispenserBehaviour((EndlessBucket) ModItems.ENDLESS_LAVA_BUCKET));
    }

    private static DispenserBehavior createDispenserBehaviour(EndlessBucket item) {
        return new ItemDispenserBehavior() {
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                FluidModificationItem fluidModificationItem = (FluidModificationItem) stack.getItem();
                BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                World world = pointer.world();

                BlockState blockState = world.getBlockState(blockPos);
                Block block = blockState.getBlock();

                if (block instanceof FluidDrainable fluidDrainable && item.fluidRef == Fluids.EMPTY) {
                    fluidDrainable.tryDrainFluid(null, world, blockPos, blockState);
                } else {
                    if (fluidModificationItem.placeFluid(null, world, blockPos, null)) {
                        fluidModificationItem.onEmptied(null, world, stack, blockPos);
                    }
                }
                return new ItemStack(item);
            }
        };
    }

    public static void registerBucketBehavior() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_LAVA_BUCKET, EndlessBucketBehaviourRegister::tryFillWithLava);
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_LAVA_BUCKET, EndlessBucketBehaviourRegister::tryFillWithLava);
        CauldronBehavior.LAVA_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_LAVA_BUCKET, EndlessBucketBehaviourRegister::tryFillWithLava);
        CauldronBehavior.POWDER_SNOW_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_LAVA_BUCKET, EndlessBucketBehaviourRegister::tryFillWithLava);

        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_WATER_BUCKET, EndlessBucketBehaviourRegister::tryFillWithWater);
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_WATER_BUCKET, EndlessBucketBehaviourRegister::tryFillWithWater);
        CauldronBehavior.LAVA_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_WATER_BUCKET, EndlessBucketBehaviourRegister::tryFillWithWater);
        CauldronBehavior.POWDER_SNOW_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_WATER_BUCKET, EndlessBucketBehaviourRegister::tryFillWithWater);

        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_BUCKET, EndlessBucketBehaviourRegister::tryEmpty);
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_BUCKET, EndlessBucketBehaviourRegister::tryEmpty);
        CauldronBehavior.LAVA_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_BUCKET, EndlessBucketBehaviourRegister::tryEmpty);
        CauldronBehavior.POWDER_SNOW_CAULDRON_BEHAVIOR.map().put(ModItems.ENDLESS_BUCKET, EndlessBucketBehaviourRegister::tryEmpty);
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

    static ActionResult emptyCauldron(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        if (state.equals(Blocks.CAULDRON.getDefaultState())) {
            return ActionResult.PASS;
        }

        if (!world.isClient) {
            Item item = stack.getItem();
            player.incrementStat(Stats.USE_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
            if (state.equals(Blocks.LAVA_CAULDRON.getDefaultState())) {
                world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else {
                world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            world.emitGameEvent((Entity)null, GameEvent.FLUID_PICKUP, pos);
        }
        return ActionResult.SUCCESS;
    }

    private static ActionResult tryFillWithWater(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        return fillCauldron(world, pos, player, hand, stack, (BlockState)Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3), SoundEvents.ITEM_BUCKET_EMPTY);
    }

    private static ActionResult tryFillWithLava(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        return (isUnderwater(world, pos) ? ActionResult.PASS : fillCauldron(world, pos, player, hand, stack, Blocks.LAVA_CAULDRON.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY_LAVA));
    }

    private static ActionResult tryEmpty(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        return emptyCauldron(state, world, pos, player, stack);
    }
}
