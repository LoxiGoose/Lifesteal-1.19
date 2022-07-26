package net.goose.lifesteal.world.gen;

import net.goose.lifesteal.Blocks.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum OreType {
    HEART_ORE(Lazy.of(ModBlocks.HEART_ORE), 10, 12, 50, 10),
    NETHERRACK_HEART_ORE(Lazy.of(ModBlocks.NETHERRACK_HEART_ORE), 12, 12, 50, 12);


    private final Lazy<Block> Block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;
    private final int veinsPerChunk;

    OreType(Lazy<net.minecraft.world.level.block.Block> block, int maxVeinSize, int minHeight, int maxHeight, int veinsPerChunk) {
        Block = block;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.veinsPerChunk = veinsPerChunk;
    }

    public Lazy<Block> getBlock() {
        return Block;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getVeinsPerChunk() {
        return veinsPerChunk;
    }

    public static OreType get(Block block){
        for (OreType ore : values()){
            if(block == ore.Block){
                return ore;
            }
        }
        return null;
    }
}
