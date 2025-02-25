package com.tethik.frieren.gameplay;

import com.tethik.frieren.Frieren;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class WorldMana {
    private static final Map<Vec3i, Integer> regionManaValues = new HashMap<>(); // needs permanent storage

    public static int getMana(Vec3i regionKey) {
        if (!regionManaValues.containsKey(regionKey)) {
            regionManaValues.put(regionKey, 1024); // TODO random default value
        }
        return regionManaValues.get(regionKey);
    }

    public static void add(Vec3i regionKey, int amount) {
        Frieren.LOGGER.info("Mana at region " + regionKey.toShortString() + " += " + amount);
        regionManaValues.put(regionKey, getMana(regionKey) + amount);
    }

//    public static void onThreshold(BlockPos pos, World world) {
//        RegistryEntry<Biome> biome = world.getBiome(pos);
//        biome.value().getSpawnSettings().getCreatureSpawnProbability();
//    }

}
