package org.me.ByBlueHeart.HDebugClient.Modules.World

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.features.module.modules.world.Fucker
import net.blueheart.hdebug.value.FloatValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

@ModuleInfo(name = "SpeedMine", description = "Allows you to mine blocks faster.", category = ModuleCategory.WORLD)
class SpeedMine : Module() {

    private val modeValue = ListValue("Mode",arrayOf("Potion","Normal"),"Normal")
    private val breakDamage = FloatValue("NormalSpeed", 0.8F, 0.1F, 1F)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        when (modeValue.get().toLowerCase()) {
            "normal" ->{
                mc.playerController.blockHitDelay = 0

                if (mc.playerController.curBlockDamageMP > breakDamage.get())
                    mc.playerController.curBlockDamageMP = 1F

                if (Fucker.currentDamage > breakDamage.get())
                    Fucker.currentDamage = 1F

                if (Nuker.currentDamage > breakDamage.get())
                    Nuker.currentDamage = 1F
            }
            "potion" ->{
                val item = false
                mc.thePlayer.addPotionEffect(PotionEffect(Potion.digSpeed.getId(), 100, if (item) 2 else 0))
            }
        }
    }
    override fun onDisable() {
        super.onDisable()
        when (modeValue.get().toLowerCase()) {
            "potion" -> {
                mc.thePlayer.removePotionEffect(Potion.digSpeed.getId())
            }
        }
    }
    override val tag: String?
        get() = modeValue.get()
}