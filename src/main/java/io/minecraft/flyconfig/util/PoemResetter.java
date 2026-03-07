package io.minecraft.flyconfig.util;

import io.minecraft.flyconfig.FlightManagement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PoemResetter {
    private static final Identifier END_ROOT = Identifier.of("minecraft", "end/root");
    private static final Identifier END_KILL_DRAGON = Identifier.of("minecraft", "end/kill_dragon");
    private static final Identifier END_DRAGON_EGG = Identifier.of("minecraft", "end/dragon_egg");
    private static final Identifier END_ENTER_END_GATEWAY = Identifier.of("minecraft", "end/enter_end_gateway");
    private static final Identifier END_RESPAWN_DRAGON = Identifier.of("minecraft", "end/respawn_dragon");
    private static final Identifier END_LEVITATE = Identifier.of("minecraft", "end/levitate");

    public static void resetEndAdvancements(ServerPlayerEntity player) {
        player.getAdvancementTracker().revokeCriterion(player.getServer().getAdvancementLoader().get(END_ROOT), "impossible");
        player.getAdvancementTracker().revokeCriterion(player.getServer().getAdvancementLoader().get(END_KILL_DRAGON), "impossible");
        player.getAdvancementTracker().revokeCriterion(player.getServer().getAdvancementLoader().get(END_DRAGON_EGG), "impossible");
        player.getAdvancementTracker().revokeCriterion(player.getServer().getAdvancementLoader().get(END_ENTER_END_GATEWAY), "impossible");
        player.getAdvancementTracker().revokeCriterion(player.getServer().getAdvancementLoader().get(END_RESPAWN_DRAGON), "impossible");
        player.getAdvancementTracker().revokeCriterion(player.getServer().getAdvancementLoader().get(END_LEVITATE), "impossible");

        FlightManagement.LOGGER.info("Reset end advancements for player: " + player.getName().getString());
    }

    public static void tickReset(ServerPlayerEntity player) {
        // 每 100 tick (5秒) 重置一次
        if (player.age % 100 == 0) {
            resetEndAdvancements(player);
        }
    }
}