
package net.blueheart.hdebug.features.module.modules.`fun`

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.timer.MSTimer
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.IntegerValue
import net.minecraft.entity.player.EnumPlayerModelParts
import kotlin.random.Random

@ModuleInfo(name = "SkinDerp", description = "Makes your skin blink (Requires multi-layer skin).", category = ModuleCategory.FUN)
class SkinDerp : Module() {
    private val delayValue = IntegerValue("Delay", 0, 0, 1000)
    private val hatValue = BoolValue("Hat", true)
    private val jacketValue = BoolValue("Jacket", true)
    private val leftPantsValue = BoolValue("LeftPants", true)
    private val rightPantsValue = BoolValue("RightPants", true)
    private val leftSleeveValue = BoolValue("LeftSleeve", true)
    private val rightSleeveValue = BoolValue("RightSleeve", true)

    private var prevModelParts = emptySet<EnumPlayerModelParts>()

    private val timer = MSTimer()

    override fun onEnable() {
        prevModelParts = mc.gameSettings.modelParts

        super.onEnable()
    }

    override fun onDisable() {
        // Disable all current model parts

        for (modelPart in mc.gameSettings.modelParts)
            mc.gameSettings.setModelPartEnabled(modelPart, false)

        // Enable all old model parts
        for (modelPart in prevModelParts)
            mc.gameSettings.setModelPartEnabled(modelPart, true)

        super.onDisable()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (timer.hasTimePassed(delayValue.get().toLong())) {
            if (hatValue.get())
                mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.HAT, Random.nextBoolean())
            if (jacketValue.get())
                mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.JACKET, Random.nextBoolean())
            if (leftPantsValue.get())
                mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, Random.nextBoolean())
            if (rightPantsValue.get())
                mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, Random.nextBoolean())
            if (leftSleeveValue.get())
                mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, Random.nextBoolean())
            if (rightSleeveValue.get())
                mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, Random.nextBoolean())
            timer.reset()
        }
    }
}
