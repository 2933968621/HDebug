package net.blueheart.hdebug.features.module.modules.player

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.IntegerValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.potion.Potion

@ModuleInfo(name = "Regen", description = "Regenerates your health much faster.", category = ModuleCategory.PLAYER)
class Regen : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Vanilla", "Spartan"), "Vanilla")
    private val healthValue = IntegerValue("Health", 18, 0, 20)
    private val foodValue = IntegerValue("Food", 18, 0, 20)
    private val speedValue = IntegerValue("Speed", 100, 1, 100)
    private val noAirValue = BoolValue("NoAir", false)
    private val potionEffectValue = BoolValue("PotionEffect", false)

    private var resetTimer = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (resetTimer)
            mc.timer.timerSpeed = 1F
        resetTimer = false

        if ((!noAirValue.get() || mc.thePlayer.onGround) && !mc.thePlayer.capabilities.isCreativeMode &&
                mc.thePlayer.foodStats.foodLevel > foodValue.get() && mc.thePlayer.isEntityAlive && mc.thePlayer.health < healthValue.get()) {
            if(potionEffectValue.get() && !mc.thePlayer.isPotionActive(Potion.regeneration))
                return
            when (modeValue.get().toLowerCase()) {
                "vanilla" -> {
                    repeat(speedValue.get()) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer())
                    }
                }

                "spartan" -> {
                    if (MovementUtils.isMoving() || !mc.thePlayer.onGround)
                        return

                    repeat(9) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer())
                    }

                    mc.timer.timerSpeed = 0.45F
                    resetTimer = true
                }
            }
        }
    }
}