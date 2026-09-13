package com.liubang.crossbreeding.event;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.block.CrossbreedingCropBlock;
import com.liubang.crossbreeding.block.CrossbreedingCropBlockEntity;
import com.liubang.crossbreeding.command.CrossbreedingCommand;
import com.liubang.crossbreeding.command.SimpleModeData;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.core.Phenotype;
import com.liubang.crossbreeding.network.SimpleModeSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Crossbreeding.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CrossbreedingCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!event.getItemStack().is(Items.STONE_HOE)) return;

        BlockPos pos = event.getPos();
        ServerLevel level = player.serverLevel();

        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof CrossbreedingCropBlock)) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            if (belowState.getBlock() instanceof CrossbreedingCropBlock) {
                pos = below;
                state = belowState;
            } else {
                return;
            }
        }

        int age = state.getValue(CrossbreedingCropBlock.AGE);
        if (age != CrossbreedingCropBlock.MAX_AGE) return;

        if (!(level.getBlockEntity(pos) instanceof CrossbreedingCropBlockEntity be)) {
            player.sendSystemMessage(Component.literal("（该小麦暂无遗传数据）"));
            return;
        }
        Genome genome = be.getGenome();
        if (genome == null) {
            player.sendSystemMessage(Component.literal("（该小麦暂无遗传数据）"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("表型：").append(Phenotype.describe(genome));

        boolean simple = SimpleModeData.get(player.getServer()).isSimpleMode();
        if (simple) {
            sb.append("\n基因型：").append(genome.toString());
        }

        player.sendSystemMessage(Component.literal(sb.toString()));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        boolean simple = SimpleModeData.get(player.getServer()).isSimpleMode();
        PacketDistributor.sendToPlayer(player, new SimpleModeSyncPayload(simple));
    }
}