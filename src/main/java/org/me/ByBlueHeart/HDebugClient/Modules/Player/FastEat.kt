package net.blueheart.hdebug.features.module.modules.player

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.MoveEvent
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.timer.MSTimer
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue
import net.blueheart.hdebug.value.IntegerValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.item.ItemBucketMilk
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemPotion
import net.minecraft.network.play.client.C03PacketPlayer

@ModuleInfo(name = "FastEat", description = "Allows you to eat items faster.", category = ModuleCategory.PLAYER)
class FastEat : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Instant", "NCP", "AAC", "Hypixel", "CustomDelay"), "NCP")

    private val noMoveValue = BoolValue("NoMove", false)

    private val delayValue = IntegerValue("CustomDelay", 0, 0, 300)
    private val customSpeedValue = IntegerValue("CustomSpeed", 2, 1, 35)
    private val customTimer = FloatValue("CustomTimer", 1.1f, 0.5f, 2f)

    private val msTimer = MSTimer()
    private var usedTimer = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F
            usedTimer = false
        }

        if (!mc.thePlayer.isUsingItem) {
            msTimer.reset()
            return
        }

        val usingItem = mc.thePlayer.itemInUse.item

        if (usingItem is ItemFood || usingItem is ItemBucketMilk || usingItem is ItemPotion) {
            when (modeValue.get().toLowerCase()) {
                "instant" -> {
                    repeat(35) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    }

                    mc.playerController.onStoppedUsingItem(mc.thePlayer)
                }

                "ncp" -> if (mc.thePlayer.itemInUseDuration > 14) {
                    repeat(20) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    }

                    mc.playerController.onStoppedUsingItem(mc.thePlayer)
                }

                "aac" -> {
                    mc.timer.timerSpeed = 1.22F
                    usedTimer = true
                }

                "hypixel" -> {
                    mc.timer.timerSpeed = 1.25F
                    usedTimer = true
                }

                "customdelay" -> {
                    mc.timer.timerSpeed = customTimer.get()
                    usedTimer = true

                    if (!msTimer.hasTimePassed(delayValue.get().toLong()))
                        return

                    repeat(customSpeedValue.get()) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                    }

                    msTimer.reset()
                }
            }
        }
    }

    @EventTarget
    fun onMove(event: MoveEvent?) {
        if (event == null) return

        if (!state || !mc.thePlayer.isUsingItem || !noMoveValue.get()) return
        val usingItem = mc.thePlayer.itemInUse.item
        if ((usingItem is ItemFood || usingItem is ItemBucketMilk || usingItem is ItemPotion))
            event.zero()
    }

    override fun onDisable() {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F
            usedTimer = false
        }
    }

    override val tag: String?
        get() = modeValue.get()
}