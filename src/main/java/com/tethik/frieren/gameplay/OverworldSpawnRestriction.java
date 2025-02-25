package com.tethik.frieren.gameplay;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Objects;

public class OverworldSpawnRestriction {

    public static void initialize() {
        ServerLivingEntityEvents.AFTER_DEATH.register((t, a) -> {

            World world = t.getWorld();

            if (world == null || world.isClient) {
                return;
            }

            ChunkPos cp = t.getWorld().getChunk(t.getBlockPos()).getPos();
            Vec3i region = new Vec3i(cp.getRegionX(), 0, cp.getRegionZ());

            WorldMana worldMana = WorldMana.getServerState(Objects.requireNonNull(world.getServer()));

            if (t instanceof HostileEntity) {
                worldMana.add(region, -10);
            }
            if (t instanceof PlayerEntity || t instanceof VillagerEntity) {
                worldMana.add(region, a.getAttacker() instanceof PlayerEntity ? 1000 : 100);
            }
            if (t instanceof CatEntity) {
                worldMana.add(region, 1000);
            }
        });
    }

}
