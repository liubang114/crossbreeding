package com.liubang.crossbreeding.registry;

import com.liubang.crossbreeding.Crossbreeding;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> REGISTRY =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Crossbreeding.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN =
            REGISTRY.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.crossbreeding"))
                    .icon(() -> new ItemStack(ModItems.CROSSBREEDING_SEED.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.FEMALE_GAMETE.get());
                        output.accept(ModItems.MALE_GAMETE.get());
                        output.accept(ModItems.CROSSBREEDING_SEED.get());
                        output.accept(ModItems.PLAIN_BREAD.get());
                        output.accept(ModItems.HIGH_NUTRITION_BREAD.get());
                        output.accept(ModItems.HIGH_SATURATION_BREAD.get());
                        output.accept(ModItems.HIGH_NUTRITION_SATURATION_BREAD.get());
                    })
                    .build());

    public static void register(IEventBus modBus) {
        REGISTRY.register(modBus);
    }
}