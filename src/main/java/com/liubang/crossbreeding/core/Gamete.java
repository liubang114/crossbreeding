package com.liubang.crossbreeding.core;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * 配子：六个等位基因（每对基因中的一个）。
 * 字符串表示形如 "aBcdEF"（6 个字符）。
 */
public record Gamete(char a, char b, char c, char d, char e, char f) {

    public static final Codec<Gamete> CODEC = Codec.STRING.xmap(
            Gamete::fromString,
            Gamete::toString
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Gamete> STREAM_CODEC =
            StreamCodec.of(
                    (buf, gamete) -> buf.writeUtf(gamete.toString()),
                    buf -> Gamete.fromString(buf.readUtf())
            );

    public static Gamete fromString(String s) {
        if (s == null || s.length() != 6) {
            throw new IllegalArgumentException("Gamete must be 6 chars: " + s);
        }
        return new Gamete(s.charAt(0), s.charAt(1), s.charAt(2),
                s.charAt(3), s.charAt(4), s.charAt(5));
    }

    @Override
    public String toString() {
        return "" + a + b + c + d + e + f;
    }

    /**
     * 单倍体翻倍：把每个等位基因复制一份，得到纯合基因型。
     * 例如 aBcdEF → aaBBccddEEFF。
     *
     * <p>用于附属模组的秋水仙素玩法：单配子 + 秋水仙素溶液 → 纯合杂交种子。
     */
    public Genome toDoubledGenome() {
        return new Genome(
                new GenePair(a, a),
                new GenePair(b, b),
                new GenePair(c, c),
                new GenePair(d, d),
                new GenePair(e, e),
                new GenePair(f, f)
        );
    }
}