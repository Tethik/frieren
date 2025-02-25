package com.tethik.frieren.mixin;

import com.tethik.frieren.Frieren;
import com.tethik.frieren.gameplay.WorldMana;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(HostileEntity.class)
public class HostileEntitySpawnRestricter {

    @Inject(method = "canSpawnInDark", at = @At("HEAD"), cancellable = true)
    private static void injectCanSpawnInDark(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> info) {
        DimensionType dimensionType = world.getDimension();
        int x = world.getChunk(pos).getPos().getRegionX();
        int z = world.getChunk(pos).getPos().getRegionZ();
        Vec3i regionKey = new Vec3i(x, 0, z);

        if (dimensionType.hasSkyLight() && world.isSkyVisible(pos)) {
            int mana = WorldMana.getMana(regionKey);
            Frieren.LOGGER.debug("Mana at region " + regionKey.toShortString() + " = " + mana);
            info.setReturnValue(WorldMana.getMana(regionKey) > 0);
        }
    }
}
