package net.blueheart.hdebug.features.module.modules.world

import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.IntegerValue

@ModuleInfo(name = "FastPlace", description = "Allows you to place blocks faster.", category = ModuleCategory.WORLD)
class FastPlace : Module() {
    val speedValue = IntegerValue("Speed", 0, 0, 4)
}
