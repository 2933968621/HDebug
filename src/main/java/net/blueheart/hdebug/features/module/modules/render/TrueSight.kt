
package net.blueheart.hdebug.features.module.modules.render

import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.BoolValue

@ModuleInfo(name = "TrueSight", description = "Allows you to see invisible entities and barriers.", category = ModuleCategory.RENDER)
class TrueSight : Module() {
    val barriersValue = BoolValue("Barriers", true)
    val entitiesValue = BoolValue("Entities", true)
}