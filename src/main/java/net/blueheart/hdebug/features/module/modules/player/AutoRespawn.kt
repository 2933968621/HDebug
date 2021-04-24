
package net.blueheart.hdebug.features.module.modules.player

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.features.module.modules.exploit.Ghost
import net.blueheart.hdebug.value.BoolValue
import net.minecraft.client.gui.GuiGameOver

@ModuleInfo(name = "AutoRespawn", description = "Automatically respawns you after dying.", category = ModuleCategory.PLAYER)
class AutoRespawn : Module() {

    private val instantValue = BoolValue("Instant", true)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (HDebug.moduleManager[Ghost::class.java]!!.state)
            return

        if (if (instantValue.get()) mc.thePlayer.health == 0F || mc.thePlayer.isDead else mc.currentScreen is GuiGameOver
                        && (mc.currentScreen as GuiGameOver).enableButtonsTimer >= 20) {
            mc.thePlayer.respawnPlayer()
            mc.displayGuiScreen(null)
        }
    }
}