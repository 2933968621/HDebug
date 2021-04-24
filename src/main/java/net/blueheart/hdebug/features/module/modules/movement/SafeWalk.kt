
package net.blueheart.hdebug.features.module.modules.movement

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.MoveEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.BoolValue

@ModuleInfo(name = "SafeWalk", description = "Prevents you from falling down as if you were sneaking.", category = ModuleCategory.MOVEMENT)
class SafeWalk : Module() {

    private val airSafeValue = BoolValue("AirSafe", false)

    @EventTarget
    fun onMove(event: MoveEvent) {
        if (airSafeValue.get() || mc.thePlayer.onGround)
            event.isSafeWalk = true
    }
}
