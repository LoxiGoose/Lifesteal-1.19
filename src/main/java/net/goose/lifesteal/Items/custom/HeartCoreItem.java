package net.goose.lifesteal.Items.custom;

import net.goose.lifesteal.Configurations.ConfigHolder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class HeartCoreItem extends Item {

    public static final Food HeartCore = (new Food.Builder()).alwaysEat().build();

    public HeartCoreItem(Properties pProperties){
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack item, World level, LivingEntity entity) {

        if(!level.isClientSide() && entity instanceof ServerPlayerEntity){

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;

            if(!ConfigHolder.SERVER.disableHeartCores.get() && entity.getHealth() < entity.getMaxHealth()){

                float MaxHealth = entity.getMaxHealth();

                entity.heal((float) (MaxHealth * ConfigHolder.SERVER.HeartCoreHeal.get()));

            }else{

                if(ConfigHolder.SERVER.disableHeartCores.get()){
                    entity.sendMessage(ITextComponent.nullToEmpty("Heart Cores have been disabled in the configurations."), entity.getUUID());
                    item.shrink(-1);
                    serverPlayer.containerMenu.broadcastChanges();
                }else{
                    entity.sendMessage(ITextComponent.nullToEmpty("You are already at max health."), entity.getUUID());
                    item.shrink(-1);
                    serverPlayer.containerMenu.broadcastChanges();
                }

            }
        }
        return super.finishUsingItem(item, level, entity);
    }
}