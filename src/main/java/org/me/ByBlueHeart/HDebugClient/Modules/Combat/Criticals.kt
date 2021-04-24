package org.me.ByBlueHeart.HDebugClient.Modules.Combat

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.*
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.ClientUtils
import net.blueheart.hdebug.utils.MathUtil
import net.blueheart.hdebug.utils.timer.MSTimer
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue
import net.blueheart.hdebug.value.IntegerValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.block.BlockLiquid
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.stats.StatList
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Flight

@ModuleInfo(name = "Criticals", description = "Automatically deals critical hits.", category = ModuleCategory.COMBAT)
class Criticals : Module() {
    var timer = MSTimer()
    var groundTicks = 0
    val modeValue = ListValue("Mode", arrayOf("Packet", "Packet2", "NCPPacket", "HypixelPacket", "AzureWare", "Legit","FakeJump", "HPacket", "Hydra", "RemixPacket", "HanaBi", "HDebugDuel", "HDebug", "ExcessiveKill", "LowClientBounce", "Sigma", "JigSaw", "Horizon", "Spartan", "Power", "HanFia", "LeainHypixel", "LeainPacket", "ETB", "Nov", "Ex", "JudGame", "Nova", "Zeroday", "Nivia2", "Nivia", "Strangth", "Health", "NoGround", "Hop", "TPHop", "Jump", "LowJump", "HYTJump", "ZQATPacket", "HuaYuTing", "AAC4", "Custom"), "Custom")
    val delayValue = IntegerValue("Delay", 0, 0, 1000)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 20)
    val CustomValue = FloatValue("CustomJump", 0.4F, 0F, 0.45F)
    var Debug = BoolValue("Debug",true)
    var DebugMessage = "§4[Debug] §f§oCrit"

    val msTimer = MSTimer()
    var nogroundstate = false

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityLivingBase) {
            val entity = event.targetEntity

            if (!mc.thePlayer.onGround || mc.thePlayer.isOnLadder || mc.thePlayer.isInWeb || mc.thePlayer.isInWater ||
                mc.thePlayer.isInLava || mc.thePlayer.ridingEntity != null || entity.hurtResistantTime >= hurtTimeValue.get() ||
                HDebug.moduleManager[Flight::class.java]!!.state || !msTimer.hasTimePassed(delayValue.get().toLong()))
                return

            val x = mc.thePlayer.posX
            val y = mc.thePlayer.posY
            val z = mc.thePlayer.posZ

            when (modeValue.get().toLowerCase()) {
                "packet" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.0625, z, true))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 1.1E-5, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, false))
                    mc.thePlayer.onCriticalHit(entity)
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "ncppacket" -> if (mc.thePlayer.ticksExisted % 9 == 0) {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.11, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.1100013579, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.0000013579, z, false))
                    mc.thePlayer.onCriticalHit(entity)
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "aac4" -> {
                    mc.thePlayer.onCriticalHit(entity)
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "legit" -> {
                    mc.thePlayer.jump()
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "fakejump" -> {
                    mc.thePlayer.isAirBorne = true;
                    mc.thePlayer.triggerAchievement(StatList.jumpStat);
                    mc.thePlayer.onGround = false;
                    mc.thePlayer.onCriticalHit(entity);
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "packet2" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.08, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "nivia2" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0233, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.2E-5, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "hypixelpacket" -> {
                    Crit3(arrayOf(0.06250999867916107, -9.999999747378752E-6, 0.0010999999940395355))
                    mc.thePlayer.onCriticalHit(entity)
                    if (Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "azureware" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(x, y + 0.0626 + 1.0E-11, z, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(x, y + 0.0626, z, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "hydra" -> {
                    val var21: Double = mc.thePlayer.posX
                    val var23: Double = mc.thePlayer.posY
                    var offset = mc.thePlayer.posZ
                    val off = 0.06259999647412312
                    val j: Double = mc.thePlayer.posY + off + MathHelper.floor_double(0.0020000000298023225).toDouble()
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(var21, var23 + off, offset, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(var21, j, offset, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(var21, var23, offset, false))
                    mc.thePlayer.onCriticalHit(entity)
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "hop" -> {
                    mc.thePlayer.motionY = 0.1
                    mc.thePlayer.fallDistance = 0.1f
                    mc.thePlayer.onGround = false
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "remixpacket" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.03 - 0.003, 0.03 + 0.003), mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.05 - 0.005, 0.05 + 0.005), mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "hanabi" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "hdebugduel" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.03 - 0.003, 0.03 + 0.003), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.02 - 0.002, 0.02 + 0.002), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "excessivekill" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.03 - 0.003, 0.03 + 0.003), mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.05 - 0.005, 0.05 + 0.005), mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.06142999976873398, 0.012511000037193298), mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "lowclientbounce" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(x, y + 0.0626 + 0.00000000001, z, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(x, y, z, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "sigma" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0626, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0626+0.00000000001, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "hdebug" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0626, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0626+0.00000000001, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "jigsaw" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "horizon" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.00000000255, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "spartan" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.04, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "nov" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.0624, 1.0-4), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "power" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(
                        C04PacketPlayerPosition(
                            mc.thePlayer.posX,
                            mc.thePlayer.posY + MathUtil.randomDouble(0.06142999976873398, 0.012511000037193298),
                            mc.thePlayer.posZ,
                            true
                        )
                    )
                    mc.thePlayer.sendQueue.addToSendQueue(
                        C04PacketPlayerPosition(
                            mc.thePlayer.posX,
                            mc.thePlayer.posY,
                            mc.thePlayer.posZ,
                            false
                        )
                    )
                    if (Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "hanfia" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.06142999976873398, 0.012511000037193298), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "leainhypixel" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.06142999976873398, 0.012511000037193298), mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "leainpacket" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.0625, 1.0E-4), mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "etb" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.7, 1.0-4), mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "ex" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.06, 0.03), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "judgame" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.05954835722479834, 0.01354835722479834), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get()) {
                        ClientUtils.displayChatMessage(DebugMessage)
                    }
                }

                "zeroday" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.051,0.0125), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "health" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.07,1.0-4), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "strangth" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.06142999976873398,0.012511000037193298), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "nivia" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "nova" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randomDouble(0.4642,0.005), mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.005, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "zqatpacket" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.00000000001, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "tphop" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.04, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "jump" -> {
                    mc.thePlayer.motionY = 0.42
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "lowjump" -> {
                    mc.thePlayer.motionY = 0.3425
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "hytjump" -> {
                    mc.thePlayer.motionY = 0.3524
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }

                "custom" -> {
                    mc.thePlayer.motionY = CustomValue.get().toDouble()
                    if(Debug.get())
                        ClientUtils.displayChatMessage(DebugMessage)
                }
            }

            msTimer.reset()
        }
    }

    fun Crit3(value: Array<Double>) {
        val curX = mc.thePlayer.posX
        val curY = mc.thePlayer.posY
        val curZ = mc.thePlayer.posZ
        val curYaw = mc.thePlayer.rotationYaw
        val curPitch = mc.thePlayer.rotationPitch
        for (offset in value)
            mc.thePlayer.sendQueue.networkManager.sendPacket(C06PacketPlayerPosLook(curX, curY + offset, curZ, curYaw, curPitch, false))
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is S08PacketPlayerPosLook)
            timer.reset()
        if (packet is C03PacketPlayer && modeValue.get().equals("NoGround", ignoreCase = true) && nogroundstate) {
            packet.onGround = false
            nogroundstate = false
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (isOnGround(0.001))
            groundTicks++
        else if (!mc.thePlayer.onGround)
            groundTicks = 0
    }

    fun isOnGround(height: Double): Boolean {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, -height, 0.0)).isEmpty()
    }

    private fun isOnWater(): Boolean {
        val y = mc.thePlayer.posY - 0.03
        for (x in MathHelper.floor_double(mc.thePlayer.posX) until MathHelper.ceiling_double_int(mc.thePlayer.posX))
            for (z in MathHelper.floor_double(mc.thePlayer.posZ) until MathHelper.ceiling_double_int(mc.thePlayer.posZ)) {
                val pos = BlockPos(x, MathHelper.floor_double(y), z)
                if (mc.theWorld.getBlockState(pos).block !is BlockLiquid) continue
                return true
            }
        return false
    }

    private fun isInLiquid(): Boolean {
        val y = mc.thePlayer.posY + 0.01
        for (x in MathHelper.floor_double(mc.thePlayer.posX) until MathHelper.ceiling_double_int(mc.thePlayer.posX))
            for (z in MathHelper.floor_double(mc.thePlayer.posZ) until MathHelper.ceiling_double_int(mc.thePlayer.posZ)) {
                val pos = BlockPos(x, y.toInt(), z)
                if (mc.theWorld.getBlockState(pos).block !is BlockLiquid)
                    continue
                return true
            }
        return false
    }

    override val tag: String
        get() = modeValue.get()
}