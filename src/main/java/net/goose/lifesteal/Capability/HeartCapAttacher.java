
package net.goose.lifesteal.Capability;

import net.goose.lifesteal.LifeSteal;
import net.goose.lifesteal.api.IHeartCap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class HeartCapAttacher {
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        final ResourceLocation IDENTIFIER = new ResourceLocation(LifeSteal.MOD_ID, "healthdifference");

        class HeartCapProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {

            private final IHeartCap backend = new HeartCap((LivingEntity) event.getObject());
            private final LazyOptional<IHeartCap> optionalData = LazyOptional.of(() -> backend);
            private final Capability<IHeartCap> capability = CapabilityRegistry.HEART_CAP_CAPABILITY;

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {

                if (cap == capability){
                    return optionalData.cast();
                }
                return LazyOptional.empty();
            }

            @Override
            public CompoundNBT serializeNBT() {
                return backend.serializeNBT();
            }

            @Override
            public void deserializeNBT(CompoundNBT nbt) {
                this.backend.deserializeNBT(nbt);
            }

        }

        final HeartCapProvider provider = new HeartCapProvider();

        event.addCapability(IDENTIFIER, provider);
    }
}