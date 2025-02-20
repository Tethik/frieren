package com.tethik.frieren;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    protected static void initialize() {
        Frieren.LOGGER.info("Registering {} components", Frieren.MOD_ID);
        // Technically this method can stay empty, but some developers like to notify
        // the console, that certain parts of the mod have been successfully initialized
    }

    public static final ComponentType<String> PLAYER_UUID_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Frieren.MOD_ID, "player_uuid"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static final ComponentType<String> CRAFTER_NAME_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Frieren.MOD_ID, "crafter_name"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

//    public static final ComponentType<Long> COOLDOWN_COMPONENT = Registry.register(
//            Registries.DATA_COMPONENT_TYPE,
//            Identifier.of(Frieren.MOD_ID, "cooldown"),
//            ComponentType.<Long>builder().codec(Codec.LONG).build()
//    );
}