package net.goose.lifesteal.Capability;

import net.goose.lifesteal.Configurations.ConfigHolder;
import net.goose.lifesteal.api.IHeartCap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameType;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Set;

public class HeartCap implements IHeartCap {
    private final LivingEntity livingEntity;

    private int defaultheartDifference = ConfigHolder.SERVER.StartingHeartDifference.get();
    private int heartDifference = defaultheartDifference;
    
    private int defaultLives = ConfigHolder.SERVER.AmountOfLives.get();
    private int lives = defaultLives;
    public HeartCap(@Nullable final LivingEntity entity) {
        this.livingEntity = entity;
    }

    @Override
    public int getHeartDifference() {
        return this.heartDifference;
    }

    @Override
    public void setHeartDifference(int hearts) {this.heartDifference = hearts;}

    @Override
    public void refreshHearts(){
        var Attribute = livingEntity.getAttribute(Attributes.MAX_HEALTH);
        Set<AttributeModifier> attributemodifiers = Attribute.getModifiers();

        if(!attributemodifiers.isEmpty()){
            Iterator<AttributeModifier> attributeModifierIterator = attributemodifiers.iterator();

            boolean FoundAttribute = false;

            while (attributeModifierIterator.hasNext()) {

                AttributeModifier attributeModifier = attributeModifierIterator.next();
                if (attributeModifier != null && attributeModifier.getName().equals("LifeStealHealthModifier")) {
                    FoundAttribute = true;

                    Attribute.removeModifier(attributeModifier);

                    AttributeModifier newmodifier = new AttributeModifier("LifeStealHealthModifier", this.heartDifference, AttributeModifier.Operation.ADDITION);

                    Attribute.addPermanentModifier(newmodifier);
                }
            }

            if(FoundAttribute == false){
                AttributeModifier attributeModifier = new AttributeModifier("LifeStealHealthModifier", this.heartDifference, AttributeModifier.Operation.ADDITION);

                Attribute.addPermanentModifier(attributeModifier);
            }
        }else{
            AttributeModifier attributeModifier = new AttributeModifier("LifeStealHealthModifier", this.heartDifference, AttributeModifier.Operation.ADDITION);

            Attribute.addPermanentModifier(attributeModifier);
        }

        if(livingEntity.getMaxHealth() <= 1 && this.heartDifference <= -20){

            if(defaultLives > 0){
                if(this.lives <= 0){
                    if (livingEntity instanceof ServerPlayer serverPlayer){
                        if(serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR){
                            serverPlayer.gameMode.changeGameModeForPlayer(GameType.SPECTATOR);

                            Component component = Component.translatable("");
                            livingEntity.sendSystemMessage(Component.translatable("You have lost all your lives and max hearts. You are now permanently dead.", component));

                            this.heartDifference = defaultheartDifference;
                            this.lives = defaultLives;

                            refreshHearts();
                        }
                    }
                }else{
                    this.lives--;

                    this.heartDifference = defaultheartDifference;
                    refreshHearts();

                    Component component = Component.translatable("");
                    livingEntity.sendSystemMessage(Component.translatable("You have lost a life. Your lives count is now "+ this.lives, component));
                }
            }else{
                if (livingEntity instanceof ServerPlayer serverPlayer){
                    if(serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR){
                        serverPlayer.gameMode.changeGameModeForPlayer(GameType.SPECTATOR);

                        Component component = Component.translatable("");
                        livingEntity.sendSystemMessage(Component.translatable("You have lost all max hearts, you are now permanently dead.", component));

                        this.heartDifference = defaultheartDifference;
                        this.lives = defaultLives;

                        refreshHearts();
                    }
                }
            }


        }else if(livingEntity.getMaxHealth() >= 40 && this.heartDifference >= 20 && defaultLives > 0 ){
            this.lives++;

            this.heartDifference = defaultheartDifference;
            refreshHearts();

            Component component = Component.translatable("");
            livingEntity.sendSystemMessage(Component.translatable("You have earned an extra life. Your lives count is now "+ this.lives, component));
        }

    }

    @Override
    public int getLives() {
        return this.lives;
    }

    @Override
    public void setLives(int lives) {this.lives = lives;}

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("heartdifference", getHeartDifference());
        tag.putInt("lives", getLives());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        setHeartDifference(tag.getInt("heartdifference"));
        setLives(tag.getInt("lives"));
    }
}
