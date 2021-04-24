
package net.blueheart.hdebug.injection.forge.mixins.entity;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.AttackEvent;
import net.blueheart.hdebug.event.ClickWindowEvent;
import net.blueheart.hdebug.features.module.modules.exploit.AbortBreaking;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
@SideOnly(Side.CLIENT)
public class MixinPlayerControllerMP {

    @Inject(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V"))
    private void attackEntity(EntityPlayer entityPlayer, Entity targetEntity, CallbackInfo callbackInfo) {
        HDebug.eventManager.callEvent(new AttackEvent(targetEntity));
    }

    @Inject(method = "getIsHittingBlock", at = @At("HEAD"), cancellable = true)
    private void getIsHittingBlock(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (HDebug.moduleManager.getModule(AbortBreaking.class).getState())
            callbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method = "windowClick", at = @At("HEAD"), cancellable = true)
    private void windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn, CallbackInfoReturnable<ItemStack> callbackInfo) {
        final ClickWindowEvent event = new ClickWindowEvent(windowId, slotId, mouseButtonClicked, mode);
        HDebug.eventManager.callEvent(event);

        if (event.isCancelled())
            callbackInfo.cancel();
    }
}