package org.me.ByBlueHeart.HDebugClient.Modules.Fun;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "AutoWatchDog", description = "Auto WatchDog Banned.", category = ModuleCategory.FUN)
public class AutoWatchDog extends Module {
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        for (int i = 0; i <= 64; i++) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, i >= 64));
        }
    }
}
