package net.goose.lifesteal.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.goose.lifesteal.api.IHeartCap;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class setHitPointDifference {

    private static final Logger LOGGER = LogManager.getLogger();
    @CapabilityInject(IHeartCap.class)
    public static final Capability<IHeartCap> HEART_CAP_CAPABILITY = null;

    public static LazyOptional<IHeartCap> getHeart(final Entity entity) {
        if (entity == null)
            return LazyOptional.empty();
        return entity.getCapability(HEART_CAP_CAPABILITY);
    }

    public setHitPointDifference(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(
                Commands.literal("setHitPointDifference")
                        .requires((commandSource) -> {return commandSource.hasPermission(2);})
                        .then(Commands.argument("Player", EntityArgument.entity())
                                .then(Commands.argument("Amount", IntegerArgumentType.integer()).executes((command) -> {
                                    return setHitPoint(command.getSource(), EntityArgument.getEntity(command, "Player"), IntegerArgumentType.getInteger(command, "Amount"));}
                                ))));
    }

    private int setHitPoint(CommandSource source, Entity chosenentity, int amount) throws CommandSyntaxException{

        String sourceTextName = source.getTextName();

        getHeart(chosenentity).ifPresent(newHeartDifference -> newHeartDifference.setHeartDifference(amount));
        getHeart(chosenentity).ifPresent(IHeartCap::refreshHearts);

        if(sourceTextName.matches("Server")){
            LOGGER.info("Set "+ chosenentity.getName().getString() +"'s HitPoint difference to "+amount);
        }else{
            LivingEntity playerthatsentcommand = source.getPlayerOrException();

            if(chosenentity != playerthatsentcommand){
                playerthatsentcommand.sendMessage(ITextComponent.nullToEmpty("Set "+ chosenentity.getName().getString() +"'s HitPoint difference to "+amount), playerthatsentcommand.getUUID());
            }
        }

        chosenentity.sendMessage(ITextComponent.nullToEmpty("Your HitPoint difference has been set to "+amount), chosenentity.getUUID());
        return 1;
    }
}