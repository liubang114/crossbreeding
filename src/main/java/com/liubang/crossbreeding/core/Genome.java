package com.liubang.crossbreeding.core;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import java.util.List;

public record Genome(
        GenePair pair1,
        GenePair pair2,
        GenePair pair3,
        GenePair pair4,
        GenePair pair5,
        GenePair pair6
) {

    public static final Codec<Genome> CODEC = Codec.STRING.xmap(
            Genome::fromString,
            Genome::toString
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Genome> STREAM_CODEC =
            StreamCodec.of(
                    (buf, genome) -> buf.writeUtf(genome.toString()),
                    buf -> Genome.fromString(buf.readUtf())
            );

    public List<GenePair> pairs() {
        return List.of(pair1, pair2, pair3, pair4, pair5, pair6);
    }

    public static Genome fromString(String s) {
        if (s == null || s.length() != 12) {
            throw new IllegalArgumentException("Genome must be 12 chars: " + s);
        }
        return new Genome(
                GenePair.fromString(s.substring(0, 2)),
                GenePair.fromString(s.substring(2, 4)),
                GenePair.fromString(s.substring(4, 6)),
                GenePair.fromString(s.substring(6, 8)),
                GenePair.fromString(s.substring(8, 10)),
                GenePair.fromString(s.substring(10, 12))
        );
    }

    @Override
    public String toString() {
        return pair1.toString() + pair2 + pair3 + pair4 + pair5 + pair6;
    }

    public boolean isDominant(Gene gene) {
        return getPair(gene).isDominant();
    }

    public boolean isRecessive(Gene gene) {
        return getPair(gene).isRecessive();
    }

    private GenePair getPair(Gene gene) {
        return switch (gene) {
            case A -> pair1;
            case B -> pair2;
            case C -> pair3;
            case D -> pair4;
            case E -> pair5;
            case F -> pair6;
        };
    }

    public Gamete createGamete(RandomSource random) {
        return new Gamete(
                pair1.randomAllele(random),
                pair2.randomAllele(random),
                pair3.randomAllele(random),
                pair4.randomAllele(random),
                pair5.randomAllele(random),
                pair6.randomAllele(random)
        );
    }

    public static Genome random(RandomSource random) {
        return new Genome(
                randomPair(random, 'A'),
                randomPair(random, 'B'),
                randomPair(random, 'C'),
                randomPair(random, 'D'),
                randomPair(random, 'E'),
                randomPair(random, 'F')
        );
    }

    private static GenePair randomPair(RandomSource random, char dominantUpper) {
        char dominant = dominantUpper;
        char recessive = Character.toLowerCase(dominantUpper);
        char first = random.nextBoolean() ? dominant : recessive;
        char second = random.nextBoolean() ? dominant : recessive;
        return new GenePair(first, second);
    }

    public static Genome combine(Gamete female, Gamete male) {
        return new Genome(
                new GenePair(female.a(), male.a()),
                new GenePair(female.b(), male.b()),
                new GenePair(female.c(), male.c()),
                new GenePair(female.d(), male.d()),
                new GenePair(female.e(), male.e()),
                new GenePair(female.f(), male.f())
        );
    }
}