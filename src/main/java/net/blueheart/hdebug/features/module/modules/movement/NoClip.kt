
package net.blueheart.hdebug.features.module.modules.movement

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo

@ModuleInfo(name = "NoClip", description = "Allows you to freely move through walls (A sandblock has to fall on your head).", category = ModuleCategory.MOVEMENT)
class NoClip : Module() {

    override fun onDisable() {
        mc.thePlayer?.noClip = false
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.noClip = true
        mc.thePlayer.fallDistance = 0f
        mc.thePlayer.onGround = false

        mc.thePlayer.capabilities.isFlying = false
        mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionY = 0.0
        mc.thePlayer.motionZ = 0.0

        val speed = 0.32f
        mc.thePlayer.jumpMovementFactor = speed
        if (mc.gameSettings.keyBindJump.isKeyDown)
            mc.thePlayer.motionY += speed.toDouble()
        if (mc.gameSettings.keyBindSneak.isKeyDown)
            mc.thePlayer.motionY -= speed.toDouble()
    }
}
