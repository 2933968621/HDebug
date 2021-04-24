package net.blueheart.hdebug.features.module.modules.`fun`

import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue

@ModuleInfo(name = "HeadDerp", description = "Makes it look like you were derping around.", category = ModuleCategory.FUN)
class Derp : Module() {
    private val headlessValue = BoolValue("Headless", false)
    private val spinnyValue = BoolValue("Spinny", false)
    private val incrementValue = FloatValue("Increment", 1F, 0F, 50F)

    private var currentSpin = 0F

    val rotation: FloatArray
        get() {
            val derpRotations = floatArrayOf(mc.thePlayer.rotationYaw + (Math.random() * 360 - 180).toFloat(), (Math.random() * 180 - 90).toFloat())

            if (headlessValue.get())
                derpRotations[1] = 180F

            if (spinnyValue.get()) {
                derpRotations[0] = currentSpin + incrementValue.get()
                currentSpin = derpRotations[0]
            }

            return derpRotations
        }
}