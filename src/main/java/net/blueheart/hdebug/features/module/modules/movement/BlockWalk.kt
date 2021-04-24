
package net.blueheart.hdebug.features.module.modules.movement

import net.blueheart.hdebug.event.BlockBBEvent
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.BoolValue
import net.minecraft.init.Blocks
import net.minecraft.util.AxisAlignedBB

@ModuleInfo(name = "BlockWalk", description = "Allows you to walk on non-fullblock blocks.", category = ModuleCategory.MOVEMENT)
class BlockWalk : Module() {
    private val cobwebValue = BoolValue("Cobweb", true)
    private val snowValue = BoolValue("Snow", true)

    @EventTarget
    fun onBlockBB(event: BlockBBEvent) {
        if (cobwebValue.get() && event.block == Blocks.web || snowValue.get() && event.block == Blocks.snow_layer)
            event.boundingBox = AxisAlignedBB.fromBounds(event.x.toDouble(), event.y.toDouble(), event.z.toDouble(),
                    event.x + 1.0, event.y + 1.0, event.z + 1.0)
    }
}
