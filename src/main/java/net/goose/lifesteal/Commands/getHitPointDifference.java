package net.goose.lifesteal.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.goose.lifesteal.api.IHeartCap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class getHitPointDifference {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Capability<IHeartCap> HEART_CAP_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static LazyOptional<IHeartCap> getHeart(final Entity entity) {
        if (entity == null)
            return LazyOptional.empty();
        return entity.getCapability(HEART_CAP_CAPABILITY);
    }

    public getHitPointDifference(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("getHitPointDifference")
                        .requires((commandSource) -> {return commandSource.hasPermission(2);})
                        .then(Commands.argument("Player", EntityArgument.entity()).executes((command) -> {
                                    return getHitPoint(command.getSource(), EntityArgument.getEntity(command, "Player"));}
                                )));
    }

    private int getHitPoint(CommandSourceStack source, Entity chosenentity) throws CommandSyntaxException{

        LivingEntity playerthatsentcommand = source.getPlayer();

        if(!source.isPlayer()){
            getHeart(chosenentity).ifPresent(HeartCap -> LOGGER.info(chosenentity.getName().getString() +"'s HitPoint difference is "+ HeartCap.getHeartDifference() + "."));
        }else{
            getHeart(chosenentity).ifPresent(HeartCap -> playerthatsentcommand.sendSystemMessage(Component.translatable(chosenentity.getName().getString() +"'s HitPoint difference is "+ HeartCap.getHeartDifference() + ".")));
        }


        return 1;
    }
}
