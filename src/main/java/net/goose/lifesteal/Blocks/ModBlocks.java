
package net.goose.lifesteal.Blocks;

import net.goose.lifesteal.Items.ModCreativeModeTab;
import net.goose.lifesteal.Items.ModItems;
import net.goose.lifesteal.LifeSteal;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.function.Supplier;

public class ModBlocks {
    private static Random random = new Random();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LifeSteal.MOD_ID);

    public static final RegistryObject<Block> HEART_CORE_BLOCK = registerBlock("heart_core_block", () ->
            new Block(AbstractBlock.Properties.of(Material.STONE)
                    .harvestLevel(2).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(6f)), ModCreativeModeTab.LIFE_TAB);
    public static final RegistryObject<Block> HEART_ORE = registerBlock("heart_ore", () ->
            new OreBlock(AbstractBlock.Properties.of(Material.STONE)
                    .harvestLevel(2).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(4f)), ModCreativeModeTab.LIFE_TAB);

    public static final RegistryObject<Block> DEEPSLATE_HEART_ORE = registerBlock("deepslate_heart_ore", () ->
            new OreBlock(AbstractBlock.Properties.of(Material.STONE)
                    .harvestLevel(2).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(8f)), ModCreativeModeTab.LIFE_TAB);

    public static final RegistryObject<Block> NETHERRACK_HEART_ORE = registerBlock("netherrack_heart_ore", () ->
            new OreBlock(AbstractBlock.Properties.of(Material.STONE)
                    .harvestLevel(2).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).strength(2f)), ModCreativeModeTab.LIFE_TAB);

    public static final RegistryObject<Block> ENDSTONE_HEART_ORE = registerBlock("endstone_heart_ore", () ->
            new OreBlock(AbstractBlock.Properties.of(Material.STONE)
                    .strength(10f).requiresCorrectToolForDrops()), ModCreativeModeTab.LIFE_TAB);

    public static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, ItemGroup tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    };

    private static <T extends  Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, ItemGroup tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}