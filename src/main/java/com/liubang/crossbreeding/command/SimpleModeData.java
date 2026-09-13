package com.liubang.crossbreeding.command;

import com.liubang.crossbreeding.Crossbreeding;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * 持久化简单模式开关的世界数据。
 *
 * <p>数据以 NBT 格式写入主世界的 data 文件夹，跨重启自动恢复。
 * 修改后必须调用 setDirty()，否则不会写入磁盘。
 */
public class SimpleModeData extends SavedData {

    private static final String DATA_NAME = Crossbreeding.MOD_ID + "_simple_mode";
    private static final String KEY_SIMPLE = "SimpleMode";

    /** 默认关闭，符合需求中“默认不开启”。 */
    private boolean simpleMode = false;

    // ---------- 构造 ----------

    public SimpleModeData() {
    }

    public SimpleModeData(boolean simpleMode) {
        this.simpleMode = simpleMode;
    }

    // ---------- 读写访问 ----------

    public boolean isSimpleMode() {
        return simpleMode;
    }

    public void setSimpleMode(boolean value) {
        if (this.simpleMode != value) {
            this.simpleMode = value;
            setDirty(); // 通知游戏数据已变化，需要写入磁盘
        }
    }

    // ---------- 序列化 ----------

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean(KEY_SIMPLE, simpleMode);
        return tag;
    }

    public static SimpleModeData load(CompoundTag tag, HolderLookup.Provider registries) {
        SimpleModeData data = new SimpleModeData();
        if (tag.contains(KEY_SIMPLE)) {
            data.simpleMode = tag.getBoolean(KEY_SIMPLE);
        }
        return data;
    }

    // ---------- 获取实例 ----------

    /**
     * 从服务器获取（或首次创建）SimpleModeData 实例。
     *
     * <p>绑定到主世界的 DimensionDataStorage，保证全局唯一且跨维度一致。
     * 文件路径为：world/data/crossbreeding_simple_mode.dat
     */
    public static SimpleModeData get(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        SimpleModeData::new,   // 新建实例
                        SimpleModeData::load   // 从 NBT 加载
                ),
                DATA_NAME
        );
    }
}