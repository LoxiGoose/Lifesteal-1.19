package net.goose.lifesteal.Items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab LIFE_TAB = new CreativeModeTab("lifesteal") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.HEART_CRYSTAL.get());
        }
    };

}