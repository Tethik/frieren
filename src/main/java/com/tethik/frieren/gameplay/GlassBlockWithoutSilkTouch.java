package com.tethik.frieren.gameplay;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;

public class GlassBlockWithoutSilkTouch {

    public static final Block[] GLASS_BLOCKS = new Block[] {
            Blocks.GLASS,
            Blocks.WHITE_STAINED_GLASS,
            Blocks.ORANGE_STAINED_GLASS,
            Blocks.MAGENTA_STAINED_GLASS,
            Blocks.LIGHT_BLUE_STAINED_GLASS,
            Blocks.YELLOW_STAINED_GLASS,
            Blocks.LIME_STAINED_GLASS,
            Blocks.PINK_STAINED_GLASS,
            Blocks.GRAY_STAINED_GLASS,
            Blocks.LIGHT_GRAY_STAINED_GLASS,
            Blocks.CYAN_STAINED_GLASS,
            Blocks.PURPLE_STAINED_GLASS,
            Blocks.BLUE_STAINED_GLASS,
            Blocks.BROWN_STAINED_GLASS,
            Blocks.GREEN_STAINED_GLASS,
            Blocks.RED_STAINED_GLASS,
            Blocks.BLACK_STAINED_GLASS,
            Blocks.GLASS_PANE,
            Blocks.WHITE_STAINED_GLASS_PANE,
            Blocks.ORANGE_STAINED_GLASS_PANE,
            Blocks.MAGENTA_STAINED_GLASS_PANE,
            Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
            Blocks.YELLOW_STAINED_GLASS_PANE,
            Blocks.LIME_STAINED_GLASS_PANE,
            Blocks.PINK_STAINED_GLASS_PANE,
            Blocks.GRAY_STAINED_GLASS_PANE,
            Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
            Blocks.CYAN_STAINED_GLASS_PANE,
            Blocks.PURPLE_STAINED_GLASS_PANE,
            Blocks.BLUE_STAINED_GLASS_PANE,
            Blocks.BROWN_STAINED_GLASS_PANE,
            Blocks.GREEN_STAINED_GLASS_PANE,
            Blocks.RED_STAINED_GLASS_PANE,
            Blocks.BLACK_STAINED_GLASS_PANE };

    public static void initialize() {
        Map<RegistryKey<LootTable>, Block> glassBlocks = new HashMap<RegistryKey<LootTable>, Block>();
        for (Block block : GLASS_BLOCKS) {
            if (block.getLootTableKey().isPresent()) {
                glassBlocks.put(block.getLootTableKey().get(), block);
            }
        }

        LootTableEvents.REPLACE.register((key, original, source, idk) -> {
            Block block = glassBlocks.getOrDefault(key, null);
            if (block != null) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(block.asItem()));

                return LootTable.builder().pool(poolBuilder.build()).build();
            }

            return null;
        });
    }
}
