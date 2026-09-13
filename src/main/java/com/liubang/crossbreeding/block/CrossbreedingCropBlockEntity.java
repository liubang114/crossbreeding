package com.liubang.crossbreeding.block;

import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrossbreedingCropBlockEntity extends BlockEntity {

    private static final String KEY_GENOME = "Genome";

    private Genome genome;

    public CrossbreedingCropBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CROSSBREEDING_CROP.get(), pos, state);
    }

    public @Nullable Genome getGenome() { return genome; }
    public void setGenome(Genome genome) { this.genome = genome; setChanged(); }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (genome != null) tag.putString(KEY_GENOME, genome.toString());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(KEY_GENOME)) {
            genome = Genome.fromString(tag.getString(KEY_GENOME));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}