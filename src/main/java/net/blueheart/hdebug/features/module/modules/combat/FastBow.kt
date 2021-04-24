
package net.blueheart.hdebug.features.module.modules.combat

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.RotationUtils
import net.blueheart.hdebug.value.IntegerValue
import net.minecraft.item.ItemBow
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

@ModuleInfo(name = "FastBow", description = "Turns your bow into a machine gun.", category = ModuleCategory.COMBAT)
class FastBow : Module() {
    private val packetsValue = IntegerValue("Packets", 20, 3, 20)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!mc.thePlayer.isUsingItem)
            return

        if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().item is ItemBow) {
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.currentEquippedItem, 0F, 0F, 0F))

            val yaw = if (RotationUtils.targetRotation != null)
                RotationUtils.targetRotation.yaw
            else
                mc.thePlayer.rotationYaw

            val pitch = if (RotationUtils.targetRotation != null)
                RotationUtils.targetRotation.pitch
            else
                mc.thePlayer.rotationPitch

            for (i in 0 until packetsValue.get())
                mc.netHandler.addToSendQueue(C05PacketPlayerLook(yaw, pitch, true))

            mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
            mc.thePlayer.itemInUseCount = mc.thePlayer.inventory.getCurrentItem().maxItemUseDuration - 1
        }
    }
}