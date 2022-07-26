package net.goose.lifesteal.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;


public interface IHeartCap extends INBTSerializable<CompoundNBT> {
    int getHeartDifference();

    void setHeartDifference(int hearts);

    void refreshHearts();

    int getLives();

    void setLives(int lives);

}