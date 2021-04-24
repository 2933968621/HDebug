package net.blueheart.hdebug.features.module.modules.combat

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.Render3DEvent
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.EntityUtils
import net.blueheart.hdebug.utils.RotationUtils
import net.blueheart.hdebug.utils.render.RenderUtils
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemBow
import java.awt.Color

@ModuleInfo(name = "BowAimbot", description = "Automatically aims at players when using a bow.", category = ModuleCategory.COMBAT)
class BowAimbot : Module() {
    private val silentValue = BoolValue("Silent", true)
    private val predictValue = BoolValue("Predict", true)
    private val throughWallsValue = BoolValue("ThroughWalls", false)
    private val predictSizeValue = FloatValue("PredictSize", 2F, 0.1F, 5F)
    private val priorityValue = ListValue("Priority", arrayOf("Health", "Distance", "Direction"), "Direction")
    private val markValue = BoolValue("Mark", true)

    private var target: Entity? = null

    override fun onDisable() {
        target = null
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        target = null

        if (mc.thePlayer.itemInUse?.item is ItemBow) {
            val entity = getTarget(throughWallsValue.get(), priorityValue.get()) ?: return

            target = entity
            RotationUtils.faceBow(target, silentValue.get(), predictValue.get(), predictSizeValue.get())
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (target != null && markValue.get())
            RenderUtils.drawPlatform(target, Color(37, 126, 255, 70))
    }

    private fun getTarget(throughWalls: Boolean, priorityMode: String): Entity? {
        val targets = mc.theWorld.loadedEntityList.filter {
            it is EntityLivingBase && EntityUtils.isSelected(it, true) &&
                    (throughWalls || mc.thePlayer.canEntityBeSeen(it))
        }

        return when (priorityMode.toUpperCase()) {
            "DISTANCE" -> targets.minBy { mc.thePlayer.getDistanceToEntity(it) }
            "DIRECTION" -> targets.minBy { RotationUtils.getRotationDifference(it) }
            "HEALTH" -> targets.minBy { (it as EntityLivingBase).health }
            else -> null
        }
    }

    fun hasTarget() = target != null && mc.thePlayer.canEntityBeSeen(target)
}