package net.blueheart.hdebug.features.module.modules.render

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo

@ModuleInfo(name = "NoBob", description = "Disables the view bobbing effect.", category = ModuleCategory.RENDER)
class NoBob : Module() {

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.distanceWalkedModified = 0f
    }
}
