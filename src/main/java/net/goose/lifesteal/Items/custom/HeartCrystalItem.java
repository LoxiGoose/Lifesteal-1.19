package net.goose.lifesteal.Items.custom;

import net.goose.lifesteal.Configurations.ConfigHolder;
import net.goose.lifesteal.api.IHeartCap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class HeartCrystalItem extends Item {

    public static final Food HeartCrystal = (new Food.Builder()).alwaysEat().build();

    @CapabilityInject(IHeartCap.class)
    public static final Capability<IHeartCap> HEART_CAP_CAPABILITY = null;

    public HeartCrystalItem(Properties pProperties){
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack item, World level, LivingEntity entity) {

        if(!level.isClientSide() && entity instanceof ServerPlayerEntity){

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;

            if(!ConfigHolder.SERVER.disableHeartCrystals.get()){

                serverPlayer.getCapability(HEART_CAP_CAPABILITY).ifPresent(newHeartDifference -> newHeartDifference.setHeartDifference(newHeartDifference.getHeartDifference() + ConfigHolder.SERVER.HeartCrystalAmountGain.get()));

                serverPlayer.getCapability(HEART_CAP_CAPABILITY).ifPresent(IHeartCap::refreshHearts);

                serverPlayer.heal(serverPlayer.getMaxHealth());

            }else{
                entity.sendMessage(ITextComponent.nullToEmpty("Heart Crystals have been disabled in the configurations."), entity.getUUID());
                item.shrink(-1);
                serverPlayer.containerMenu.broadcastChanges();

            }
        }
        return super.finishUsingItem(item, level, entity);
    }
}