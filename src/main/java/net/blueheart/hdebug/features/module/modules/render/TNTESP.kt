package net.blueheart.hdebug.features.module.modules.render

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.Render3DEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.render.RenderUtils
import net.minecraft.entity.item.EntityTNTPrimed
import java.awt.Color

@ModuleInfo(name = "TNTESP", description = "Allows you to see ignited TNT blocks through walls.", category = ModuleCategory.RENDER)
class TNTESP : Module() {

    @EventTarget
    fun onRender3D(event : Render3DEvent) {
        mc.theWorld.loadedEntityList.filterIsInstance<EntityTNTPrimed>().forEach { RenderUtils.drawEntityBox(it, Color.RED, false) }
    }
}