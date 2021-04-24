
package net.blueheart.hdebug.features.module.modules.combat

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.StrafeEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.EntityUtils
import net.blueheart.hdebug.utils.Rotation
import net.blueheart.hdebug.utils.RotationUtils
import net.blueheart.hdebug.utils.extensions.getDistanceToEntityBox
import net.blueheart.hdebug.utils.misc.RandomUtils
import net.blueheart.hdebug.utils.timer.MSTimer
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue
import kotlin.random.Random

@ModuleInfo(name = "AimBot", description = "Automatically faces selected entities around you.", category = ModuleCategory.COMBAT)
class Aimbot : Module() {
    private val rangeValue = FloatValue("Range", 4.4F, 1F, 8F)
    private val turnSpeedValue = FloatValue("TurnSpeed", 2F, 1F, 180F)
    private val fovValue = FloatValue("FOV", 180F, 1F, 180F)
    private val centerValue = BoolValue("Center", false)
    private val lockValue = BoolValue("Lock", true)
    private val onClickValue = BoolValue("OnClick", false)
    private val jitterValue = BoolValue("Jitter", false)

    private val clickTimer = MSTimer()

    @EventTarget
    fun onStrafe(event: StrafeEvent) {
        if (mc.gameSettings.keyBindAttack.isKeyDown)
            clickTimer.reset()

        if (onClickValue.get() && clickTimer.hasTimePassed(500L))
            return

        val range = rangeValue.get()
        val entity = mc.theWorld.loadedEntityList
                .filter {
                    EntityUtils.isSelected(it, true) && mc.thePlayer.canEntityBeSeen(it) &&
                            mc.thePlayer.getDistanceToEntityBox(it) <= range && RotationUtils.getRotationDifference(it) <= fovValue.get()
                }
                .minBy { RotationUtils.getRotationDifference(it) } ?: return

        if (!lockValue.get() && RotationUtils.isFaced(entity, range.toDouble()))
            return

        val rotation = RotationUtils.limitAngleChange(
                Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch),
                if (centerValue.get())
                    RotationUtils.toRotation(RotationUtils.getCenter(entity.entityBoundingBox), true)
                else
                    RotationUtils.searchCenter(entity.entityBoundingBox, false, false, true,
                            false).rotation,
                (turnSpeedValue.get() + Math.random()).toFloat()
        )

        rotation.toPlayer(mc.thePlayer)

        if (jitterValue.get()) {
            val yaw = Random.nextBoolean()
            val pitch = Random.nextBoolean()
            val yawNegative = Random.nextBoolean()
            val pitchNegative = Random.nextBoolean()

            if (yaw)
                mc.thePlayer.rotationYaw += if (yawNegative) -RandomUtils.nextFloat(0F, 1F) else RandomUtils.nextFloat(0F, 1F)

            if (pitch) {
                mc.thePlayer.rotationPitch += if (pitchNegative) -RandomUtils.nextFloat(0F, 1F) else RandomUtils.nextFloat(0F, 1F)
                if (mc.thePlayer.rotationPitch > 90)
                    mc.thePlayer.rotationPitch = 90F
                else if (mc.thePlayer.rotationPitch < -90)
                    mc.thePlayer.rotationPitch = -90F
            }
        }
    }
}