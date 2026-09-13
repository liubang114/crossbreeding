package com.liubang.crossbreeding.core;

/**
 * 六个基因位点的定义。
 *
 * <p>每个位点有一个显性等位基因（大写）和一个隐性等位基因（小写），
 * 以及对应的显性/隐性表型中文名称。
 *
 * <p>显隐性关系：
 * <ul>
 *   <li>A 显性 = 少穗，a 隐性 = 多穗</li>
 *   <li>B 显性 = 抗病，b 隐性 = 不抗病</li>
 *   <li>C 显性 = 低饱食，c 隐性 = 高饱食</li>
 *   <li>D 显性 = 低饱和，d 隐性 = 高饱和</li>
 *   <li>E 显性 = 多壳，e 隐性 = 少壳</li>
 *   <li>F 显性 = 生长不旺盛，f 隐性 = 生长旺盛</li>
 * </ul>
 */
public enum Gene {
    A('A', 'a', "少穗", "多穗"),
    B('B', 'b', "抗病", "不抗病"),
    C('C', 'c', "低饱食", "高饱食"),
    D('D', 'd', "低饱和", "高饱和"),
    E('E', 'e', "多壳", "少壳"),
    F('F', 'f', "生长不旺盛", "生长旺盛");

    public final char dominant;
    public final char recessive;
    public final String dominantName;
    public final String recessiveName;

    Gene(char dominant, char recessive, String dominantName, String recessiveName) {
        this.dominant = dominant;
        this.recessive = recessive;
        this.dominantName = dominantName;
        this.recessiveName = recessiveName;
    }

    /** 根据等位基因字符找到对应的 Gene 位点。 */
    public static Gene fromAllele(char allele) {
        char upper = Character.toUpperCase(allele);
        for (Gene g : values()) {
            if (g.dominant == upper) return g;
        }
        throw new IllegalArgumentException("Unknown allele: " + allele);
    }
}