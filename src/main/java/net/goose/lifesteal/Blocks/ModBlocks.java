
package net.goose.lifesteal.Blocks;

import net.goose.lifesteal.Items.ModCreativeModeTab;
import net.goose.lifesteal.Items.ModItems;
import net.goose.lifesteal.LifeSteal;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LifeSteal.MOD_ID);

    public static final RegistryObject<Block> HEART_CORE_BLOCK = registerBlock("heart_core_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()), ModCreativeModeTab.LIFE_TAB);
    public static final RegistryObject<Block> HEART_ORE = registerBlock("heart_ore", () ->
            new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(4f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)), ModCreativeModeTab.LIFE_TAB);

    public static final RegistryObject<Block> DEEPSLATE_HEART_ORE = registerBlock("deepslate_heart_ore", () ->
            new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(8f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)), ModCreativeModeTab.LIFE_TAB);

    public static final RegistryObject<Block> NETHERRACK_HEART_ORE = registerBlock("netherrack_heart_ore", () ->
            new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2f).requiresCorrectToolForDrops().explosionResistance(999f), UniformInt.of(5, 9)), ModCreativeModeTab.LIFE_TAB);

    public static final RegistryObject<Block> ENDSTONE_HEART_ORE = registerBlock("endstone_heart_ore", () ->
            new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(10f).requiresCorrectToolForDrops().explosionResistance(999f), UniformInt.of(5, 9)), ModCreativeModeTab.LIFE_TAB);

    public static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    };

    private static <T extends  Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}