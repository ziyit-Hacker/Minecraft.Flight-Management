package io.minecraft.flyconfig.event;

import io.minecraft.flyconfig.FlightManagement;
import io.minecraft.flyconfig.util.PoemResetter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoemLoopHandler {
    private static final Map<UUID, Boolean> loopModePlayers = new HashMap<>();
    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(PoemLoopHandler::onServerTick);
        FlightManagement.LOGGER.info("Poem loop handler registered");
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;

        if (tickCounter % 100 == 0) {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (isLoopModeEnabled(player)) {
                    PoemResetter.resetEndAdvancements(player);
                    FlightManagement.LOGGER.info("Auto-reset poem for: " + player.getName().getString());
                }
            }
        }
    }

    public static void enableLoopMode(ServerPlayerEntity player) {
        loopModePlayers.put(player.getUuid(), true);
    }

    public static void disableLoopMode(ServerPlayerEntity player) {
        loopModePlayers.remove(player.getUuid());
    }

    public static boolean isLoopModeEnabled(ServerPlayerEntity player) {
        return loopModePlayers.getOrDefault(player.getUuid(), false);
    }
}