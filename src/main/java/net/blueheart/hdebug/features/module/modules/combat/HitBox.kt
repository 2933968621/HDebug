package net.blueheart.hdebug.features.module.modules.combat

import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.FloatValue

@ModuleInfo(name = "HitBox", description = "Makes hitboxes of targets bigger.", category = ModuleCategory.COMBAT)
class HitBox : Module() {
    val sizeValue = FloatValue("Size", 0.4F, 0F, 1F)
}