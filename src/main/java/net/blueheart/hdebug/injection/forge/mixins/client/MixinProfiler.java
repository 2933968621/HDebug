
package net.blueheart.hdebug.injection.forge.mixins.client;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.Render2DEvent;
import net.blueheart.hdebug.utils.ClassUtils;
import net.minecraft.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Profiler.class)
public class MixinProfiler {

    @Inject(method = "startSection", at = @At("HEAD"))
    private void startSection(String name, CallbackInfo callbackInfo) {
        if(name.equals("bossHealth") && ClassUtils.hasClass("net.labymod.api.LabyModAPI"))
            HDebug.eventManager.callEvent(new Render2DEvent(0F));
    }
}