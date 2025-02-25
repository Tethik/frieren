package com.tethik.frieren;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.tethik.frieren.gameplay.WorldMana;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModCommands {

    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("mana").executes(ModCommands::executeReadManaCommand)
                    .then(CommandManager.argument("newMana", IntegerArgumentType.integer(0, 10000))
                            .requires(source -> source.hasPermissionLevel(1))
                            .executes(ModCommands::executeSetManaCommand))
            );
        });
    }

    private static int executeReadManaCommand(CommandContext<ServerCommandSource> context) {
        World world = context.getSource().getWorld();
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 0;
        }

        BlockPos pos = BlockPos.ofFloored(player.getPos());
        WorldMana mana = WorldMana.getServerState(world.getServer());

        context.getSource().sendFeedback(() ->
                Text.literal("Mana in this region: " + mana.get(world, pos)), false);

        return 1;
    }

    private static int executeSetManaCommand(CommandContext<ServerCommandSource> context) {
        World world = context.getSource().getWorld();
        PlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 0;
        }

        int newMana = IntegerArgumentType.getInteger(context, "newMana");

        BlockPos pos = BlockPos.ofFloored(player.getPos());
        WorldMana mana = WorldMana.getServerState(world.getServer());

        mana.set(world, pos, newMana);

        context.getSource().sendFeedback(() -> Text.literal("New mana: " + newMana), false);
        return 1;
    }
}
