package net.goose.lifesteal.enchantment;

import net.goose.lifesteal.LifeSteal;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, LifeSteal.MOD_ID);

    public static RegistryObject<Enchantment> LIFESTEAL =
            ENCHANTMENTS.register("lifesteal",
                    ()-> new LifeStealEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND));

    public static void register(IEventBus eventBus){
        ENCHANTMENTS.register(eventBus);
    }
}
