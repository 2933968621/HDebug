package net.blueheart.hdebug.features.module.modules.movement

import net.blueheart.hdebug.event.EventState
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.MotionEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils

@ModuleInfo(name = "Strafe", description = "Allows you to freely move in mid air.", category = ModuleCategory.MOVEMENT)
class Strafe : Module() {

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (event.eventState == EventState.POST)
            return

        MovementUtils.strafe()
    }
}
