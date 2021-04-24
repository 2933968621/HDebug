
package net.blueheart.hdebug.features.module.modules.combat

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.BoolValue
import net.minecraft.init.Items
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

@ModuleInfo(name = "AutoBow", description = "Automatically shoots an arrow whenever your bow is fully loaded.", category = ModuleCategory.COMBAT)
class AutoBow : Module() {
    private val waitForBowAimbot = BoolValue("WaitForBowAimbot", true)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val bowAimbot = HDebug.moduleManager[BowAimbot::class.java] as BowAimbot

        if (mc.thePlayer.isUsingItem && mc.thePlayer.heldItem?.item == Items.bow &&
                mc.thePlayer.itemInUseDuration > 20 && (!waitForBowAimbot.get() || !bowAimbot.state || bowAimbot.hasTarget())) {
            mc.thePlayer.stopUsingItem()
            mc.netHandler.addToSendQueue(C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
        }
    }
}
