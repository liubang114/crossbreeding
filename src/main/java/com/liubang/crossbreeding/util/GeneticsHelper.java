package com.liubang.crossbreeding.util;

import com.liubang.crossbreeding.block.CrossbreedingCropBlockEntity;
import com.liubang.crossbreeding.core.Genome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * 遗传数据访问辅助类。
 *
 * <p>由于遗传数据存储在 BlockEntity 中，本类只是对
 * {@code level.getBlockEntity(pos)} 的便捷封装。
 */
public final class GeneticsHelper {

    private GeneticsHelper() {
    }

    /** 从世界坐标读取作物的 Genome。 */
    public static Genome getGenome(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CrossbreedingCropBlockEntity be) {
            return be.getGenome();
        }
        return null;
    }

    /** 将 Genome 写入指定坐标的 BlockEntity。 */
    public static void setGenome(Level level, BlockPos pos, Genome genome) {
        if (level.getBlockEntity(pos) instanceof CrossbreedingCropBlockEntity be) {
            be.setGenome(genome);
        }
    }
}