package org.me.ByBlueHeart.HDebugClient.Modules.Player;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.ClientUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "FlagDetector", description = "Allows you to see flags.", category = ModuleCategory.PLAYER)
public class FlagDetector extends Module {

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            ClientUtils.displayChatMessage("¡ì7[¡ì9VIO¡ì7] You ¡ìffailed ¡ìbIllegalMove");
        }
    }
}