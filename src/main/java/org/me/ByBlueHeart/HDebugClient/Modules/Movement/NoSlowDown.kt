package org.me.ByBlueHeart.HDebugClient.Modules.Movement

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.*
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.timer.MSTimer
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.item.*
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.MathHelper
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.Aura
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils

@ModuleInfo(name = "NoSlowDown", description = "Cancels slowness effects caused by soulsand and using items.", category = ModuleCategory.MOVEMENT)
class NoSlowDown : Module() {

    private val ModeValue = ListValue("Mode", arrayOf("Vanilla", "Packet", "Packet2", "Hypixel", "AAC"), "Vanilla")

    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)

    // Soulsand
    val soulsandValue = BoolValue("Soulsand", true)

    var timer = MSTimer()

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val heldItem = mc.thePlayer.heldItem
        if (heldItem == null || heldItem.item !is ItemSword || !MovementUtils.isMoving()) {
            return
        }
        val killAura = HDebug.moduleManager[Aura::class.java] as Aura
        if (!mc.thePlayer.isBlocking && !killAura.blockingStatus) {
            return
        }
        when (ModeValue.get().toLowerCase()) {
            "hypixel" -> {
                when (event.eventState){
                    EventState.PRE -> {
                        if (mc.thePlayer.isBlocking() && MovementUtils.isMoving()) {
                            mc.netHandler.networkManager.sendPacket(C07PacketPlayerDigging(
                                C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-.8, -.8, -.8), EnumFacing.DOWN))
                        }
                    }
                    EventState.POST -> {
                        if (mc.thePlayer.isBlocking() && MovementUtils.isMoving()) {
                            mc.thePlayer.sendQueue.addToSendQueue(C08PacketPlayerBlockPlacement(
                                BlockPos(-.8, -.8, -.8), 255, mc.thePlayer.getCurrentEquippedItem(), 0F, 0F, 0F))
                        }
                    }
                }
            }
            "packet" -> {
                when (event.eventState) {
                    EventState.PRE -> {
                        val digging = C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN)
                        mc.netHandler.addToSendQueue(digging)
                    }
                    EventState.POST -> {
                        val blockPlace = C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem())
                        mc.netHandler.addToSendQueue(blockPlace)
                    }
                }
            }
            "packet2" -> {
                if (killAura.target == null) {
                    when (event.eventState) {
                        EventState.PRE -> {
                            val digging = C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN)
                            mc.netHandler.addToSendQueue(digging)
                        }
                        EventState.POST -> {
                            mc.thePlayer.sendQueue.addToSendQueue(C08PacketPlayerBlockPlacement(getHypixelBlockpos(), 255,
                                    mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f))
                        }
                    }
                }
            }
            "aac" -> {
                when (event.eventState) {
                    EventState.PRE -> {
                        mc.thePlayer.sendQueue.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
                    }
                    EventState.POST -> {
                        mc.thePlayer.sendQueue.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255,
                                mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f))
                        //return
                    }
                }
            }
        }
    }

    fun getHypixelBlockpos(): BlockPos {
        val random = java.util.Random()
        val dx = MathHelper.floor_double(random.nextDouble() / 1000 + 2820)
        val jy = MathHelper.floor_double(random.nextDouble() / 100 * 0.20000000298023224)
        val kz = MathHelper.floor_double(random.nextDouble() / 1000 + 2820)
        return BlockPos(dx, -jy % 255, kz)
    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer.heldItem?.item

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }

    private fun getMultiplier(item: Item?, isForward: Boolean) = when (item) {
        is ItemFood, is ItemPotion, is ItemBucketMilk -> {
            if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
        }
        is ItemSword -> {
            if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
        }
        is ItemBow -> {
            if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
        }
        else -> 0.2F
    }

    override val tag: String?
        get() = ModeValue.get()
}
