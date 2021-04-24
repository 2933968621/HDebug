
package net.blueheart.hdebug.features.module.modules.movement

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo

@ModuleInfo(name = "Freeze", description = "Allows you to stay stuck in mid air.", category = ModuleCategory.MOVEMENT)
class Freeze : Module() {
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.isDead = true
        mc.thePlayer.rotationYaw = mc.thePlayer.cameraYaw
        mc.thePlayer.rotationPitch = mc.thePlayer.cameraPitch
    }

    override fun onDisable() {
        mc.thePlayer?.isDead = false
    }
}
