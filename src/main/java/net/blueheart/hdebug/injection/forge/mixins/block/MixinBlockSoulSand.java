
package net.blueheart.hdebug.injection.forge.mixins.block;

import net.blueheart.hdebug.HDebug;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.NoSlowDown;
import net.minecraft.block.BlockSoulSand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSoulSand.class)
@SideOnly(Side.CLIENT)
public class MixinBlockSoulSand {

    @Inject(method = "onEntityCollidedWithBlock", at = @At("HEAD"), cancellable = true)
    private void onEntityCollidedWithBlock(CallbackInfo callbackInfo) {
        final NoSlowDown noSlow = (NoSlowDown) HDebug.moduleManager.getModule(NoSlowDown.class);

        if(noSlow.getState() && noSlow.getSoulsandValue().get())
            callbackInfo.cancel();
    }
}