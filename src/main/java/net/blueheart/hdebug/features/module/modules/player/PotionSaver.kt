package net.blueheart.hdebug.features.module.modules.player

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.PacketEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.*

@ModuleInfo(name = "PotionSaver", description = "Freezes all potion effects while you are standing still.", category = ModuleCategory.PLAYER)
class PotionSaver : Module() {

    @EventTarget
    fun onPacket(e: PacketEvent) {
        val packet = e.packet

        if (packet is C03PacketPlayer && packet !is C04PacketPlayerPosition && packet !is C06PacketPlayerPosLook &&
                packet !is C05PacketPlayerLook && mc.thePlayer != null && !mc.thePlayer.isUsingItem)
            e.cancelEvent()
    }

}