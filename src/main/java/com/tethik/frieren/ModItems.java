package com.tethik.frieren;

import com.tethik.frieren.item.EndlessBucket;
import com.tethik.frieren.item.EndlessBucketBehaviourRegister;
import com.tethik.frieren.item.RingOfBondedLocation;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final RegistryKey<Item> ENDLESS_LAVA_BUCKET_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Frieren.MOD_ID, "endless_lava_bucket"));
    public static final Item ENDLESS_LAVA_BUCKET = register(
            new EndlessBucket(Fluids.LAVA, new Item.Settings().
                    registryKey(ModItems.ENDLESS_LAVA_BUCKET_KEY).
                    fireproof().
                    rarity(Rarity.RARE).
                    maxCount(1)),
            ENDLESS_LAVA_BUCKET_KEY
    );

    public static final RegistryKey<Item> ENDLESS_WATER_BUCKET_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Frieren.MOD_ID, "endless_water_bucket"));
    public static final Item ENDLESS_WATER_BUCKET = register(
            new EndlessBucket(Fluids.WATER, new Item.Settings().
                    registryKey(ModItems.ENDLESS_WATER_BUCKET_KEY).
                    fireproof().
                    rarity(Rarity.RARE).
                    maxCount(1)),
            ENDLESS_WATER_BUCKET_KEY
    );

    public static final RegistryKey<Item> ENDLESS_BUCKET_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Frieren.MOD_ID, "endless_bucket"));
    public static final Item ENDLESS_BUCKET = register(
            new EndlessBucket(Fluids.EMPTY, new Item.Settings().
                    registryKey(ModItems.ENDLESS_BUCKET_KEY).
                    fireproof().
                    rarity(Rarity.RARE).
                    maxCount(1)),
            ENDLESS_BUCKET_KEY
    );

    public static final RegistryKey<Item> RING_OF_BONDED_LOCATION_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Frieren.MOD_ID, "ring_of_bonded_location"));
    public static final Item RING_OF_BONDED_LOCATION = register(
            new RingOfBondedLocation(new Item.Settings().
                    registryKey(ModItems.RING_OF_BONDED_LOCATION_KEY).
                    rarity(Rarity.RARE).
                    maxCount(1).
                    useCooldown(30 * 60)),
            RING_OF_BONDED_LOCATION_KEY
    );

    public static void initialize() {
        // Endless Bucket
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(ModItems.ENDLESS_BUCKET));

        // Endless Lava Bucket
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(ModItems.ENDLESS_LAVA_BUCKET));

        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(ModItems.ENDLESS_LAVA_BUCKET, 30 * 20);
        });

        // Endless Water Bucket
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
            .register((itemGroup) -> itemGroup.add(ModItems.ENDLESS_WATER_BUCKET));

        // Ring
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(ModItems.RING_OF_BONDED_LOCATION));

        EndlessBucketBehaviourRegister.registerBucketBehavior();
        EndlessBucketBehaviourRegister.registerDispenserBehavior();
        EndlessBucketBehaviourRegister.registerChestLoot();
    }

    public static Item register(Item item, RegistryKey<Item> registryKey) {
        // Register the item.
        return Registry.register(Registries.ITEM, registryKey.getValue(), item);
    }
}