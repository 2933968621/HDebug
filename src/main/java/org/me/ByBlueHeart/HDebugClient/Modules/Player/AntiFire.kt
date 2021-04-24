package org.me.ByBlueHeart.HDebugClient.Modules.Player

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.minecraft.network.play.client.C03PacketPlayer

@ModuleInfo(name = "AntiFire", description = "Removes Fire.", category = ModuleCategory.PLAYER)
class AntiFire : Module() {
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.isBurning)
            for (i in 0..9)
                mc.netHandler.addToSendQueue(C03PacketPlayer())
    }
}