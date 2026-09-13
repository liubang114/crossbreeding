package com.liubang.crossbreeding.core;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;

/**
 * 一对等位基因，例如 "Aa"、"BB"、"cc"。
 * 每对基因由两个字符表示，显性大写，隐性小写。
 */
public record GenePair(char first, char second) {

    public static final Codec<GenePair> CODEC = Codec.STRING.xmap(
            GenePair::fromString,
            GenePair::toString
    );

    /** 从两个字符构造，自动校验大小写。 */
    public static GenePair fromString(String s) {
        if (s == null || s.length() != 2) {
            throw new IllegalArgumentException("GenePair must be exactly 2 chars: " + s);
        }
        return new GenePair(s.charAt(0), s.charAt(1));
    }

    @Override
    public String toString() {
        return "" + first + second;
    }

    /** 是否表现显性性状。任一等位基因为大写即为显性。 */
    public boolean isDominant() {
        return Character.isUpperCase(first) || Character.isUpperCase(second);
    }

    /** 是否表现隐性性状（即两个等位基因都是隐性）。 */
    public boolean isRecessive() {
        return Character.isLowerCase(first) && Character.isLowerCase(second);
    }

    /** 从这对基因中随机抽取一个等位基因，用于生成配子。 */
    public char randomAllele(RandomSource random) {
        return random.nextBoolean() ? first : second;
    }

    /** 获取该基因对对应的 Gene 位点。 */
    public Gene gene() {
        return Gene.fromAllele(Character.toUpperCase(first));
    }
}