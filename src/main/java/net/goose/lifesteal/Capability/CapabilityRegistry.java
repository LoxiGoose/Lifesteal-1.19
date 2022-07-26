package net.goose.lifesteal.Capability;

import net.goose.lifesteal.Commands.getHitPointDifference;
import net.goose.lifesteal.Commands.getLives;
import net.goose.lifesteal.Commands.setHitPointDifference;
import net.goose.lifesteal.Commands.setLives;
import net.goose.lifesteal.Configurations.ConfigHolder;
import net.goose.lifesteal.LifeSteal;
import net.goose.lifesteal.api.IHeartCap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

public class CapabilityRegistry {

    @CapabilityInject(IHeartCap.class)
    public static final Capability<IHeartCap> HEART_CAP_CAPABILITY = null;

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static void registerCapabilities(){
        CapabilityManager.INSTANCE.register(
                IHeartCap.class,
                CapabilityStorage.create(() -> HEART_CAP_CAPABILITY, Constants.NBT.TAG_COMPOUND),
                HeartCap::new
        );
    }

    public static LazyOptional<IHeartCap> getHeart(final LivingEntity entity) {
        if (entity == null)
            return LazyOptional.empty();
        return entity.getCapability(HEART_CAP_CAPABILITY);
    }

    public static LazyOptional<IHeartCap> getHeart(final Entity entity) {
        if (entity == null)
            return LazyOptional.empty();
        return entity.getCapability(HEART_CAP_CAPABILITY);
    }



    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = LifeSteal.MOD_ID)

    public static class EventHandler {

        @SubscribeEvent
        public static void OnCommandsRegister(RegisterCommandsEvent event){
            new getHitPointDifference(event.getDispatcher());
            new setHitPointDifference(event.getDispatcher());
            new getLives(event.getDispatcher());
            new setLives(event.getDispatcher());

            ConfigCommand.register(event.getDispatcher());
        }

        @SubscribeEvent
        public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof PlayerEntity) {
                HeartCapAttacher.attach(event);
            }
        }

        @SubscribeEvent
        public static void playerJoinEvent(PlayerEvent.PlayerLoggedInEvent event){
            Entity newPlayer = event.getEntity();

            getHeart(newPlayer).ifPresent(IHeartCap::refreshHearts);
        }

        @SubscribeEvent
        public static void playerCloneEvent(PlayerEvent.Clone event){

            boolean wasDeath = event.isWasDeath();

            LivingEntity oldPlayer = event.getOriginal();
            oldPlayer.revive();
            LivingEntity newPlayer = event.getEntityLiving();

            if(wasDeath && !ConfigHolder.SERVER.disableHeartLoss.get()) {
                int amountOfHealthLossUponLoss = ConfigHolder.SERVER.amountOfHealthLostUponLoss.get();
                if(!ConfigHolder.SERVER.loseHeartsOnlyWhenKilledByPlayer.get()){
                    getHeart(oldPlayer).ifPresent(oldHeartDifference -> getHeart(newPlayer).ifPresent(newHeartDifference ->
                            newHeartDifference.setHeartDifference(oldHeartDifference.getHeartDifference() - amountOfHealthLossUponLoss)
                    ));

                    getHeart(newPlayer).ifPresent(IHeartCap::refreshHearts);

                    newPlayer.setHealth(newPlayer.getMaxHealth());
                }else if(!ConfigHolder.SERVER.disableLifesteal.get()){

                    LivingEntity KillerEntity = oldPlayer.getLastHurtByMob();

                    if(KillerEntity instanceof PlayerEntity){
                        DamageSource damageSource = oldPlayer.getLastDamageSource();

                        if(damageSource == null){
                            getHeart(oldPlayer).ifPresent(oldHeartDifference -> getHeart(newPlayer).ifPresent(newHeartDifference ->
                                    newHeartDifference.setHeartDifference(oldHeartDifference.getHeartDifference() - amountOfHealthLossUponLoss)
                            ));
                            getHeart(newPlayer).ifPresent(IHeartCap::refreshHearts);

                            newPlayer.heal(newPlayer.getMaxHealth());
                        }else if(damageSource.getEntity() instanceof PlayerEntity){
                            getHeart(oldPlayer).ifPresent(oldHeartDifference -> getHeart(newPlayer).ifPresent(newHeartDifference ->
                                    newHeartDifference.setHeartDifference(oldHeartDifference.getHeartDifference() - amountOfHealthLossUponLoss)
                            ));
                            getHeart(newPlayer).ifPresent(IHeartCap::refreshHearts);

                            newPlayer.heal(newPlayer.getMaxHealth());
                        }
                    }

                }
            }else{
                getHeart(oldPlayer).ifPresent(oldHeartDifference -> getHeart(newPlayer).ifPresent(newHeartDifference ->
                        newHeartDifference.setHeartDifference(oldHeartDifference.getHeartDifference())
                ));

                getHeart(newPlayer).ifPresent(IHeartCap::refreshHearts);

                newPlayer.heal(newPlayer.getMaxHealth());
            }


        }

        @SubscribeEvent
        public static void deathEvent(LivingDeathEvent event){

            LivingEntity killedEntity = event.getEntityLiving();

            if(killedEntity instanceof PlayerEntity || ConfigHolder.SERVER.shouldAllMobsGiveHearts.get()){
                LivingEntity killerEntity = killedEntity.getLastHurtByMob();

                if(killerEntity != null){

                    if(killerEntity instanceof PlayerEntity && !ConfigHolder.SERVER.disableLifesteal.get()){
                        DamageSource damageSource = killedEntity.getLastDamageSource();
                        int amountOfHealthLostUponLoss = ConfigHolder.SERVER.amountOfHealthLostUponLoss.get();
                        AtomicInteger HeartDifference = new AtomicInteger();
                        getHeart(killedEntity).ifPresent(HeartCap -> HeartDifference.set(HeartCap.getHeartDifference()));

                        if(ConfigHolder.SERVER.playersGainHeartsifKillednoHeart.get()){
                            if(damageSource == null){
                                getHeart(killerEntity).ifPresent(newHeartDifference -> newHeartDifference.setHeartDifference(newHeartDifference.getHeartDifference() + amountOfHealthLostUponLoss));

                                getHeart(killerEntity).ifPresent(IHeartCap::refreshHearts);

                            }else if(damageSource.getEntity() instanceof PlayerEntity){
                                getHeart(killerEntity).ifPresent(newHeartDifference -> newHeartDifference.setHeartDifference(newHeartDifference.getHeartDifference() + amountOfHealthLostUponLoss));

                                getHeart(killerEntity).ifPresent(IHeartCap::refreshHearts);
                            }
                        }else{

                            if(!ConfigHolder.SERVER.disableHeartLoss.get()){
                                if(ConfigHolder.SERVER.minimumamountofheartscanhave.get() > -1){
                                    if(ConfigHolder.SERVER.startingHeartDifference.get() + HeartDifference.get() > -ConfigHolder.SERVER.minimumamountofheartscanhave.get()){
                                        if(damageSource == null){
                                            getHeart(killerEntity).ifPresent(newHeartDifference -> newHeartDifference.setHeartDifference(newHeartDifference.getHeartDifference() + amountOfHealthLostUponLoss));

                                            getHeart(killerEntity).ifPresent(IHeartCap::refreshHearts);

                                        }else if(damageSource.getEntity() instanceof PlayerEntity){
                                            getHeart(killerEntity).ifPresent(newHeartDifference -> newHeartDifference.setHeartDifference(newHeartDifference.getHeartDifference() + amountOfHealthLostUponLoss));

                                            getHeart(killerEntity).ifPresent(IHeartCap::refreshHearts);
                                        }

                                    }else{
                                        killerEntity.sendMessage(ITextComponent.nullToEmpty("This player doesn't have any hearts you can steal."), killerEntity.getUUID());
                                    }
                                }else{
                                    if(damageSource == null){
                                        getHeart(killerEntity).ifPresent(newHeartDifference -> newHeartDifference.setHeartDifference(newHeartDifference.getHeartDifference() + amountOfHealthLostUponLoss));

                                        getHeart(killerEntity).ifPresent(IHeartCap::refreshHearts);

                                    }else if(damageSource.getEntity() instanceof PlayerEntity){
                                        getHeart(killerEntity).ifPresent(newHeartDifference -> newHeartDifference.setHeartDifference(newHeartDifference.getHeartDifference() + amountOfHealthLostUponLoss));

                                        getHeart(killerEntity).ifPresent(IHeartCap::refreshHearts);
                                    }
                                }
                            }

                        }
                    }

                }

            }
        }
    }
}