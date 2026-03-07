package io.minecraft.flyconfig.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.minecraft.flyconfig.util.PoemResetter;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PoemCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("poem")
                        .then(CommandManager.literal("reset")
                                .executes(PoemCommand::resetSelf)
                        )
                        .then(CommandManager.literal("loop")
                                .executes(PoemCommand::startLoop)
                        )
                        .then(CommandManager.literal("stop")
                                .executes(PoemCommand::stopLoop)
                        )
        );
    }

    private static int resetSelf(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player != null) {
            PoemResetter.resetEndAdvancements(player);
            source.sendFeedback(() ->
                            Text.literal("§a已重置末地通关数据！再去打龙就能看终末之诗了！"),
                    false
            );
        }
        return 1;
    }

    private static int startLoop(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player != null) {
            source.sendFeedback(() ->
                            Text.literal("§a已开启终末之诗循环模式！每次打完龙都能看！"),
                    false
            );
        }
        return 1;
    }

    private static int stopLoop(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player != null) {
            source.sendFeedback(() ->
                            Text.literal("§c已关闭终末之诗循环模式"),
                    false
            );
        }
        return 1;
    }
}