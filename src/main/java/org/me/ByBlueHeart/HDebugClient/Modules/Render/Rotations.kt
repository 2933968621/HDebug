/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.PacketEvent
import net.blueheart.hdebug.event.Render3DEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.features.module.modules.`fun`.Derp
import net.blueheart.hdebug.features.module.modules.combat.BowAimbot
import net.blueheart.hdebug.features.module.modules.world.CivBreak
import net.blueheart.hdebug.features.module.modules.world.Fucker
import net.blueheart.hdebug.utils.RotationUtils
import net.blueheart.hdebug.value.BoolValue
import net.minecraft.network.play.client.C03PacketPlayer
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.Aura
import org.me.ByBlueHeart.HDebugClient.Modules.World.*

@ModuleInfo(name = "Rotations", description = "Allows you to see server-sided head and body rotations.", category = ModuleCategory.RENDER)
class Rotations : Module() {

    private val bodyValue = BoolValue("Body", true)

    private var playerYaw: Float? = null

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (RotationUtils.serverRotation != null && !bodyValue.get())
            mc.thePlayer.rotationYawHead = RotationUtils.serverRotation.yaw
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (!bodyValue.get() || !shouldRotate() || mc.thePlayer == null)
            return

        val packet = event.packet
        if (packet is C03PacketPlayer.C06PacketPlayerPosLook || packet is C03PacketPlayer.C05PacketPlayerLook) {
            playerYaw = (packet as C03PacketPlayer).yaw
            mc.thePlayer.renderYawOffset = packet.getYaw()
            mc.thePlayer.rotationYawHead = packet.getYaw()
        } else {
            if (playerYaw != null)
                mc.thePlayer.renderYawOffset = this.playerYaw!!
            mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset
        }
    }

    private fun getState(module: Class<*>) = HDebug.moduleManager[module]!!.state

    private fun shouldRotate(): Boolean {
        val killAura = HDebug.moduleManager.getModule(Aura::class.java) as Aura
        return getState(Scaffold::class.java) || getState(BlockFly::class.java) || getState(HypixelBlockFly::class.java) || getState(Tower::class.java) ||
                (getState(Aura::class.java) && killAura.target != null) ||
                getState(Derp::class.java) || getState(BowAimbot::class.java) ||
                getState(Fucker::class.java) || getState(CivBreak::class.java) || getState(Nuker::class.java) ||
                getState(ChestAura::class.java)
    }
}
