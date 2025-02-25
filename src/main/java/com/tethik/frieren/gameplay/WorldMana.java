package com.tethik.frieren.gameplay;

import com.tethik.frieren.Frieren;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.*;

public class WorldMana extends PersistentState {
    private final Map<Vec3i, Integer> regionManaValues = new HashMap<>(); // needs permanent storage

    public int get(Vec3i regionKey) {
        if (!regionManaValues.containsKey(regionKey)) {
            Random random = new Random();
            regionManaValues.put(regionKey, random.nextInt(3000)); // TODO random default value
        }
        return regionManaValues.get(regionKey);
    }

    public void add(Vec3i regionKey, int amount) {
        Frieren.LOGGER.info("Mana at region " + regionKey.toShortString() + " += " + amount);
        int newMana = get(regionKey) + amount;
        if (newMana < 0) {
            newMana = 0;
        } else if (newMana > 10000) {
            newMana = 10000;
        }
        regionManaValues.put(regionKey, newMana);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList regionKeys = new NbtList();
        List<Integer> manaValues = new ArrayList<>();

        for (Vec3i key : regionManaValues.keySet()) {
            regionKeys.add(new NbtIntArray(new int []{ key.getX(), key.getY(), key.getZ() }));
            manaValues.add(regionManaValues.get(key));
            Frieren.LOGGER.info("saved to nbt: " + key.toShortString() + " " + regionManaValues.get(key));
        }

        nbt.putIntArray("manaValues", manaValues);
        nbt.put("regionKeys", regionKeys);
        Frieren.LOGGER.info("finished saving to nbt");
        return nbt;
    }

    public static WorldMana createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        WorldMana state = new WorldMana();
        NbtList regionKeys = (NbtList) tag.get("regionKeys");
        int[] manaValues = tag.getIntArray("manaValues");

        for (int i = 0; i < Objects.requireNonNull(regionKeys).size(); i++) {
            NbtIntArray regionKey = (NbtIntArray) regionKeys.get(i);
            NbtInt x = regionKey.get(0);
            NbtInt y = regionKey.get(1);
            NbtInt z = regionKey.get(2);
            Vec3i key = new Vec3i(x.intValue(), y.intValue(), z.intValue());
            state.regionManaValues.put(key, manaValues[i]);
            Frieren.LOGGER.info("loaded from nbt: " + key.toShortString() + " " + manaValues[i]);
        }

        Frieren.LOGGER.info("finished loading from nbt");

        return state;
    }

    public static WorldMana createNew() {
        return new WorldMana();
    }

    private static final Type<WorldMana> type = new Type<>(
            WorldMana::createNew, // If there's no 'StateSaverAndLoader' yet create one and refresh variables
            WorldMana::createFromNbt, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );

    public static WorldMana getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        PersistentStateManager persistentStateManager = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();

        WorldMana state = persistentStateManager.getOrCreate(type, Frieren.MOD_ID);

        // If state is not marked dirty, when Minecraft closes, 'writeNbt' won't be called and therefore nothing will be saved.
        // Technically it's 'cleaner' if you only mark state as dirty when there was actually a change, but the vast majority
        // of mod writers are just going to be confused when their data isn't being saved, and so it's best just to 'markDirty' for them.
        // Besides, it's literally just setting a bool to true, and the only time there's a 'cost' is when the file is written to disk when
        // there were no actual change to any of the mods state (INCREDIBLY RARE).
        state.markDirty();

        return state;
    }

}
