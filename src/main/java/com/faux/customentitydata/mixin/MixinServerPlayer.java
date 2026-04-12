package com.faux.customentitydata.mixin;

import com.faux.customentitydata.api.ICustomDataHolder;
import com.faux.customentitydata.api.IPersistentDataHolder;
import com.faux.customentitydata.api.PersistentEntityDataConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer implements IPersistentDataHolder {

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    public void restoreFrom(ServerPlayer oldPlayer, boolean restoreAll, CallbackInfo ci) {

        IPersistentDataHolder oldHolder = (IPersistentDataHolder) oldPlayer;
        this.faux$setPersistentData(oldHolder.faux$getPersistentData());
    }

    @Override
    public CompoundTag faux$getPersistentData() {
        CompoundTag lifetimeData = ((ICustomDataHolder) this).faux$getCustomData();
        if (!lifetimeData.contains(PersistentEntityDataConstants.PERSISTENT_NBT_KEY)) {
            CompoundTag tag = new CompoundTag();
            lifetimeData.put(PersistentEntityDataConstants.PERSISTENT_NBT_KEY, tag);
            return tag;
        }
        return lifetimeData.getCompoundOrEmpty(PersistentEntityDataConstants.PERSISTENT_NBT_KEY);
    }

    @Override
    public void faux$setPersistentData(CompoundTag tag) {
        ((ICustomDataHolder) this).faux$getCustomData().put(PersistentEntityDataConstants.PERSISTENT_NBT_KEY, tag);
    }
}
