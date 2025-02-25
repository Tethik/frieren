package com.tethik.frieren.gameplay;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;

public class OverworldSpawnRestriction {

    public static void initialize() {
        ServerLivingEntityEvents.AFTER_DEATH.register((t, a) -> {
            ChunkPos cp = t.getWorld().getChunk(t.getBlockPos()).getPos();
            Vec3i region = new Vec3i(cp.getRegionX(), 0, cp.getRegionZ());

            if (t instanceof HostileEntity) {
                WorldMana.add(region, -10);
            }
            if (t instanceof PlayerEntity || t instanceof VillagerEntity) {
                WorldMana.add(region, a.getAttacker() instanceof PlayerEntity ? 2000 : 333);
            }
            if (t instanceof CatEntity) {
                WorldMana.add(region, 1000);
            }
        });
    }

}
