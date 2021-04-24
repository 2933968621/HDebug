package org.me.ByBlueHeart.HDebugClient.Modules.Fun

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.PacketEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.TimerUtil
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Hypixel.ChinaHypixel.isOnGround

@ModuleInfo(name = "Kill-Mother", description = "Self-Destructive.", category = ModuleCategory.FUN)
class ZiHui : Module() {
    var timer: TimerUtil? = null
    override val tag: String?
        get() = "MC"

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if(mc.thePlayer.onGround || isOnGround(0.5))
            mc.thePlayer.sendQueue.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        if(timer?.delay(65F)!!) {
            mc.thePlayer.sendQueue.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            timer?.reset();
        }
    }
}