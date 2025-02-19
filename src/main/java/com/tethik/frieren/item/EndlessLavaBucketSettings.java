package com.tethik.frieren.item;

import com.tethik.frieren.ModItems;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class EndlessLavaBucketSettings extends Item.Settings {
    Item recipeRemainder = ModItems.ENDLESS_LAVA_BUCKET;

    public EndlessLavaBucketSettings() {
        super();
        registryKey(ModItems.ENDLESS_LAVA_BUCKET_KEY);
        fireproof();
        rarity(Rarity.RARE);
        maxCount(1);
    }
}
