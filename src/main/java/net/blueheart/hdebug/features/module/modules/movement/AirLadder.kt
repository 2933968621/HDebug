package net.blueheart.hdebug.features.module.modules.movement

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.block.BlockUtils
import net.blueheart.hdebug.value.FloatValue
import net.minecraft.block.BlockLadder
import net.minecraft.block.BlockVine
import net.minecraft.util.BlockPos

@ModuleInfo(name = "AirLadder", description = "Allows you to climb up ladders/vines without touching them.", category = ModuleCategory.MOVEMENT)
class AirLadder : Module() {

    private val speedValue = FloatValue("Speed", 0.117F, 0.01F, 5F)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (BlockUtils.getBlock(BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ)) is BlockLadder && mc.thePlayer.isCollidedHorizontally ||
                BlockUtils.getBlock(BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) is BlockVine ||
                BlockUtils.getBlock(BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ)) is BlockVine) {
            mc.thePlayer.motionY = speedValue.get().toDouble()
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        }
    }
}