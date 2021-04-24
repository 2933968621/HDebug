
package net.blueheart.hdebug.features.module.modules.movement

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo

@ModuleInfo(name = "PerfectHorseJump", description = "Automatically jumps when the jump bar of a horse is filled up completely.", category = ModuleCategory.MOVEMENT)
class PerfectHorseJump : Module() {

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.horseJumpPowerCounter = 9
        mc.thePlayer.horseJumpPower = 1f
    }
}
