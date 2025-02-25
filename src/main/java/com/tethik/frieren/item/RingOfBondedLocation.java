package com.tethik.frieren.item;

import com.tethik.frieren.Frieren;
import com.tethik.frieren.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class RingOfBondedLocation extends Item {

    public RingOfBondedLocation(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return ActionResult.PASS;
        }

        ItemStack stack = user.getStackInHand(hand);

        String uuid = stack.getOrDefault(ModComponents.PLAYER_UUID_COMPONENT, "undefined");

        PlayerEntity target = user.getEntityWorld().getPlayerByUuid(UUID.fromString(uuid));
        if (target != null) {
            Frieren.LOGGER.info("Target was available");
            if (target.getUuid() != user.getUuid()) {
                world.playSound(null, BlockPos.ofFloored(target.getPos()), SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                user.teleportTo(new TeleportTarget((ServerWorld) world, target.getPos(), Vec3d.ZERO, 0.0F, 0.0F, entity -> {}));
            } else {
                user.sendMessage(Text.literal("You cannot teleport to yourself").formatted(Formatting.BLUE), false);
                world.playSound(null, BlockPos.ofFloored(user.getPos()), SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                return ActionResult.FAIL;
            }
        } else {
            user.sendMessage(Text.literal("Entity not available").formatted(Formatting.BLUE), false);
            world.playSound(null, BlockPos.ofFloored(user.getPos()), SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return ActionResult.FAIL;
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        String uuid = player.getUuidAsString();
        Frieren.LOGGER.info("Got on craft uuid" + uuid);

        stack.set(ModComponents.PLAYER_UUID_COMPONENT, uuid);
        stack.set(ModComponents.CRAFTER_NAME_COMPONENT, player.getNameForScoreboard());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        String uuid = stack.getOrDefault(ModComponents.CRAFTER_NAME_COMPONENT, "unknown");
        tooltip.add(Text.translatable("item.frieren.ring.info", uuid).formatted(Formatting.GOLD));
    }
}
