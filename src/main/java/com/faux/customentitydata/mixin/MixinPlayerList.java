package com.faux.customentitydata.mixin;

import com.faux.customentitydata.api.playersaves.IPlayerLoadListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Final
    @Shadow
    private PlayerDataStorage playerIo;

    @Inject(method = "load", at = @At("RETURN"))
    private void onPlayerLoad(ServerPlayer player, ProblemReporter problemReporter, CallbackInfoReturnable<Optional<ValueInput>> cir) {

        if (this.playerIo instanceof AccessorPlayerDataStorage accessor) {

            IPlayerLoadListener.EVENT.invoker().loadPlayerData(player, accessor.faux$getSaveDir().toPath());
        }
    }
}