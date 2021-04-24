
package net.blueheart.hdebug.features.module.modules.player

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.BoolValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.potion.Potion

@ModuleInfo(name = "Zoot", description = "Removes all bad potion effects/fire.", category = ModuleCategory.PLAYER)
class Zoot : Module() {

    private val badEffectsValue = BoolValue("BadEffects", true)
    private val noAirValue = BoolValue("NoAir", false)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (noAirValue.get() && !mc.thePlayer.onGround)
            return

        if (badEffectsValue.get())
            for (potion in mc.thePlayer.activePotionEffects)
                if (potion != null && hasBadEffect()) // TODO: Check current potion
                    for (i in 0 until potion.duration / 20)
                        mc.netHandler.addToSendQueue(C03PacketPlayer())
    }

    // TODO: Check current potion
    private fun hasBadEffect() = mc.thePlayer.isPotionActive(Potion.hunger) || mc.thePlayer.isPotionActive(Potion.moveSlowdown) ||
            mc.thePlayer.isPotionActive(Potion.digSlowdown) || mc.thePlayer.isPotionActive(Potion.harm) ||
            mc.thePlayer.isPotionActive(Potion.confusion) || mc.thePlayer.isPotionActive(Potion.blindness) ||
            mc.thePlayer.isPotionActive(Potion.weakness) || mc.thePlayer.isPotionActive(Potion.wither) || mc.thePlayer.isPotionActive(Potion.poison)

}