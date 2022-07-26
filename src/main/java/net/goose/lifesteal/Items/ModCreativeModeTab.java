package net.goose.lifesteal.Items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModCreativeModeTab {
    public static final ItemGroup LIFE_TAB = new ItemGroup("lifesteal") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.HEART_CRYSTAL.get());
        }
    };

}