package com.liubang.crossbreeding.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 从基因型推导表型的工具类。
 */
public final class Phenotype {

    private Phenotype() {
    }

    /**
     * 生成表型的中文描述字符串。
     * 例如 "少穗、抗病、高饱食、低饱和、多壳、生长不旺盛"。
     */
    public static String describe(Genome genome) {
        List<String> parts = new ArrayList<>(6);
        for (Gene gene : Gene.values()) {
            GenePair pair = getPair(genome, gene);
            parts.add(pair.isDominant() ? gene.dominantName : gene.recessiveName);
        }
        return String.join("、", parts);
    }

    private static GenePair getPair(Genome genome, Gene gene) {
        return switch (gene) {
            case A -> genome.pair1();
            case B -> genome.pair2();
            case C -> genome.pair3();
            case D -> genome.pair4();
            case E -> genome.pair5();
            case F -> genome.pair6();
        };
    }

    /** 获取某个性状是否表现显性。 */
    public static boolean isDominant(Genome genome, Gene gene) {
        return getPair(genome, gene).isDominant();
    }
}