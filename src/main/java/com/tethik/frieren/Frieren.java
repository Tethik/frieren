package com.tethik.frieren;

import com.tethik.frieren.gameplay.GlassBlockWithoutSilkTouch;
import com.tethik.frieren.gameplay.OverworldSpawnRestriction;
import com.tethik.frieren.gameplay.WorldMana;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Frieren implements ModInitializer {
	public static final String MOD_ID = "frieren";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItems.initialize();
		ModComponents.initialize();
		ModCommands.initialize();

		OverworldSpawnRestriction.initialize();
		GlassBlockWithoutSilkTouch.initialize();



		LOGGER.info("Hello Fabric world!");

	}
}