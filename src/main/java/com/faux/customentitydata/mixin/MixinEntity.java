package com.faux.customentitydata.mixin;

import com.faux.customentitydata.api.ICustomDataHolder;
import com.faux.customentitydata.api.PersistentEntityDataConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity implements ICustomDataHolder {

    @Unique
    public CompoundTag faux$persistentData;

    @Override
    public CompoundTag faux$getCustomData() {
        if (faux$persistentData == null)
            faux$persistentData = new CompoundTag();

        return faux$persistentData;
    }

    @Override
    public void faux$setCustomData(CompoundTag tag) {
        faux$persistentData = tag;
    }

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueOutput;)V", shift = At.Shift.BEFORE))
    public void saveWithoutId(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.store(PersistentEntityDataConstants.CUSTOM_NBT_KEY, CompoundTag.CODEC, faux$getCustomData());
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V", shift = At.Shift.BEFORE))
    public void load(ValueInput valueInput, CallbackInfo ci) {
        valueInput.read(PersistentEntityDataConstants.CUSTOM_NBT_KEY, CompoundTag.CODEC).ifPresent(this::faux$setCustomData);
    }

}
