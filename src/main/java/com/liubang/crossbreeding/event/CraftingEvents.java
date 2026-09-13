package com.liubang.crossbreeding.event;

import com.liubang.crossbreeding.Crossbreeding;
import com.liubang.crossbreeding.core.Gamete;
import com.liubang.crossbreeding.core.Gene;
import com.liubang.crossbreeding.core.Genome;
import com.liubang.crossbreeding.registry.ModDataComponents;
import com.liubang.crossbreeding.registry.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Crossbreeding.MOD_ID)
public class CraftingEvents {

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();
        if (!result.is(ModItems.CROSSBREEDING_SEED.get())) return;
        if (result.has(ModDataComponents.GENOME.get())) return;

        Player player = event.getEntity();
        Container input = event.getInventory();

        int femaleCount = 0;
        int maleCount = 0;
        int otherCount = 0;
        Gamete femaleGamete = null;
        Gamete maleGamete = null;

        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.FEMALE_GAMETE.get())) {
                femaleGamete = stack.get(ModDataComponents.GAMETE.get());
                femaleCount++;
            } else if (stack.is(ModItems.MALE_GAMETE.get())) {
                maleGamete = stack.get(ModDataComponents.GAMETE.get());
                maleCount++;
            } else {
                otherCount++;
            }
        }

        // 只处理恰好 1 雌 + 1 雄、没有其它物品的情况
        // 其它情况（如秋水仙素 + 单配子）交给其它模组处理
        if (femaleCount != 1 || maleCount != 1 || otherCount != 0) return;

        // 兜底：如果配子没带 GAMETE 组件，随机生成
        RandomSource random = player.level().random;
        if (femaleGamete == null) femaleGamete = randomGamete(random);
        if (maleGamete == null) maleGamete = randomGamete(random);

        Genome genome = Genome.combine(femaleGamete, maleGamete);
        result.set(ModDataComponents.GENOME.get(), genome);
    }

    private static Gamete randomGamete(RandomSource random) {
        Gene[] genes = Gene.values();
        char[] alleles = new char[genes.length];
        for (int i = 0; i < genes.length; i++) {
            alleles[i] = random.nextBoolean() ? genes[i].dominant : genes[i].recessive;
        }
        return new Gamete(alleles[0], alleles[1], alleles[2],
                alleles[3], alleles[4], alleles[5]);
    }
}