package com.liubang.crossbreeding;

/**
 * 模组配置常量。
 */
public final class Config {

    private Config() {
    }

    /** 染病概率分母：不抗病小麦在阶段 0→1 时有 1/N 概率染病。 */
    public static final int DISEASE_CHANCE_DENOMINATOR = 5;

    /** 多穗小麦额外掉落的小麦数量下限。 */
    public static final int MULTI_EAR_EXTRA_MIN = 2;

    /** 多穗小麦额外掉落的小麦数量上限。 */
    public static final int MULTI_EAR_EXTRA_MAX = 3;

    /** 少穗小麦掉落的配子数量下限。 */
    public static final int GAMETE_DROP_MIN = 2;

    /** 少穗小麦掉落的配子数量上限。 */
    public static final int GAMETE_DROP_MAX = 3;
}