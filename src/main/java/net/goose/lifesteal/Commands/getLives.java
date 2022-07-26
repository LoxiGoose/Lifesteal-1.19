
package net.goose.lifesteal.Commands;

import com.mojang.brigadier.CommandDispatcher;
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

public class getLives {

    private static final Logger LOGGER = LogManager.getLogger();
    @CapabilityInject(IHeartCap.class)
    public static final Capability<IHeartCap> HEART_CAP_CAPABILITY = null;

    public static LazyOptional<IHeartCap> getHeart(final Entity entity) {
        if (entity == null)
            return LazyOptional.empty();
        return entity.getCapability(HEART_CAP_CAPABILITY);
    }

    public getLives(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(
                Commands.literal("getLives")
                        .requires((commandSource) -> {return commandSource.hasPermission(2);})
                        .then(Commands.argument("Player", EntityArgument.entity()).executes((command) -> {
                            return getLive(command.getSource(), EntityArgument.getEntity(command, "Player"));}
                        )));
    }

    private int getLive(CommandSource source, Entity chosenentity) throws CommandSyntaxException{

        String sourceTextName = source.getTextName();

        if(sourceTextName.matches("Server")){
            getHeart(chosenentity).ifPresent(HeartCap -> LOGGER.info(chosenentity.getName().getString() +" has "+ HeartCap.getLives() + " lives."));
        }else{
            LivingEntity playerthatsentcommand = source.getPlayerOrException();
            getHeart(chosenentity).ifPresent(HeartCap -> playerthatsentcommand.sendMessage(ITextComponent.nullToEmpty(chosenentity.getName().getString() +" has "+ HeartCap.getLives() + " lives."), playerthatsentcommand.getUUID()));
        }


        return 1;
    }
}