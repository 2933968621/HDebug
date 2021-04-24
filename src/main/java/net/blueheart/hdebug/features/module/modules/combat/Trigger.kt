package net.blueheart.hdebug.features.module.modules.combat

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.Render3DEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.EntityUtils
import net.blueheart.hdebug.utils.timer.TimeUtils
import net.blueheart.hdebug.value.IntegerValue
import net.minecraft.client.settings.KeyBinding

@ModuleInfo(name = "Trigger", description = "Automatically attacks the entity you are looking at.", category = ModuleCategory.COMBAT)
class Trigger : Module() {
    private val maxCPS: IntegerValue = object : IntegerValue("MaxCPS", 8, 1, 20) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = minCPS.get()
            if (i > newValue) set(i)
            delay = TimeUtils.randomClickDelay(minCPS.get(), this.get())
        }
    }

    private val minCPS: IntegerValue = object : IntegerValue("MinCPS", 5, 1, 20) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = maxCPS.get()
            if (i < newValue) set(i)
            delay = TimeUtils.randomClickDelay(this.get(), maxCPS.get())
        }
    }

    private var delay = TimeUtils.randomClickDelay(minCPS.get(), maxCPS.get())
    private var lastSwing = 0L

    @EventTarget
    fun onRender(event: Render3DEvent) {
        if (mc.objectMouseOver != null && System.currentTimeMillis() - lastSwing >= delay &&
                EntityUtils.isSelected(mc.objectMouseOver.entityHit, true)) {
            KeyBinding.onTick(mc.gameSettings.keyBindAttack.keyCode) // Minecraft Click handling
            lastSwing = System.currentTimeMillis()
            delay = TimeUtils.randomClickDelay(minCPS.get(), maxCPS.get())
        }
    }
}