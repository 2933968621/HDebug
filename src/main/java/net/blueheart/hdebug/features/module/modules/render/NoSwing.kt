
package net.blueheart.hdebug.features.module.modules.render

import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.BoolValue

@ModuleInfo(name = "NoSwing", description = "Disabled swing effect when hitting an entity/mining a block.", category = ModuleCategory.RENDER)
class NoSwing : Module() {
    val serverSideValue = BoolValue("ServerSide", true)
}