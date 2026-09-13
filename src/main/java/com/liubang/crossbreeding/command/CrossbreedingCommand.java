package com.liubang.crossbreeding.command;

import com.liubang.crossbreeding.network.SimpleModeSyncPayload;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CrossbreedingCommand {

    private CrossbreedingCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("crossbreeding")
                .requires(source -> source.hasPermission(2));

        root.then(Commands.literal("simple")
                .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean value = BoolArgumentType.getBool(context, "value");
                            CommandSourceStack source = context.getSource();
                            MinecraftServer server = source.getServer();

                            SimpleModeData data = SimpleModeData.get(server);
                            data.setSimpleMode(value);

                            // 关键：广播给所有已连接的客户端，让它们的 SimpleModeCache 同步
                            PacketDistributor.sendToAllPlayers(new SimpleModeSyncPayload(value));

                            Component message = Component.translatable(
                                    value ? "crossbreeding.simple.on"
                                            : "crossbreeding.simple.off"
                            );
                            source.sendSuccess(() -> message, true);
                            return 1;
                        })
                )
        );

        dispatcher.register(root);
    }
}