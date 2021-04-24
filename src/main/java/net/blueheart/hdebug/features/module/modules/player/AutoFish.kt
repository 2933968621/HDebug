
package net.blueheart.hdebug.features.module.modules.player

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.timer.MSTimer
import net.minecraft.item.ItemFishingRod

@ModuleInfo(name = "AutoFish", description = "Automatically catches fish when using a rod.", category = ModuleCategory.PLAYER)
class AutoFish : Module() {

    private val rodOutTimer = MSTimer()

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.heldItem == null || mc.thePlayer.heldItem.item !is ItemFishingRod)
            return

        if (rodOutTimer.hasTimePassed(500L) && mc.thePlayer.fishEntity == null || (mc.thePlayer.fishEntity != null && mc.thePlayer.fishEntity.motionX == 0.0 && mc.thePlayer.fishEntity.motionZ == 0.0 && mc.thePlayer.fishEntity.motionY != 0.0)) {
            mc.rightClickMouse()
            rodOutTimer.reset()
        }
    }
}
