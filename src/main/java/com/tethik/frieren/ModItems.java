package com.tethik.frieren;

import com.tethik.frieren.item.EndlessBucket;
import com.tethik.frieren.item.SuspiciousSubstance;
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

    public static final RegistryKey<Item> SUSPICIOUS_SUBSTANCE_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Frieren.MOD_ID, "suspicious_substance"));
    public static final Item SUSPICIOUS_SUBSTANCE = register(
            new SuspiciousSubstance(new Item.Settings().registryKey(SUSPICIOUS_SUBSTANCE_KEY)),
            SUSPICIOUS_SUBSTANCE_KEY
    );

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

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ModItems.SUSPICIOUS_SUBSTANCE));

        // Add the suspicious substance to the composting registry with a 30% chance of increasing the composter's level.
        CompostingChanceRegistry.INSTANCE.add(ModItems.SUSPICIOUS_SUBSTANCE, 0.3f);

        // Add the suspicious substance to the registry of fuels, with a burn time of 30 seconds.
        // Remember, Minecraft deals with logical based-time using ticks.
        // 20 ticks = 1 second.
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(ModItems.SUSPICIOUS_SUBSTANCE, 2 * 20);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ModItems.ENDLESS_LAVA_BUCKET));

        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(ModItems.ENDLESS_LAVA_BUCKET, 30 * 20);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
            .register((itemGroup) -> itemGroup.add(ModItems.ENDLESS_WATER_BUCKET));

        EndlessBucket.registerBucketBehavior();
    }

    public static Item register(Item item, RegistryKey<Item> registryKey) {
        // Register the item.
        return Registry.register(Registries.ITEM, registryKey.getValue(), item);
    }
}