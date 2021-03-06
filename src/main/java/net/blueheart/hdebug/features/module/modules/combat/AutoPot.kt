
package net.blueheart.hdebug.features.module.modules.combat

import net.blueheart.hdebug.event.EventState.POST
import net.blueheart.hdebug.event.EventState.PRE
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.MotionEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.InventoryUtils
import net.blueheart.hdebug.utils.Rotation
import net.blueheart.hdebug.utils.RotationUtils
import net.blueheart.hdebug.utils.misc.FallingPlayer
import net.blueheart.hdebug.utils.misc.RandomUtils
import net.blueheart.hdebug.utils.timer.MSTimer
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue
import net.blueheart.hdebug.value.IntegerValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.item.ItemPotion
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.potion.Potion

@ModuleInfo(name = "AutoPot", description = "Automatically throws healing potions.", category = ModuleCategory.COMBAT)
class AutoPot : Module() {

    private val healthValue = FloatValue("Health", 15F, 1F, 20F)
    private val delayValue = IntegerValue("Delay", 500, 500, 1000)

    private val openInventoryValue = BoolValue("OpenInv", false)
    private val simulateInventory = BoolValue("SimulateInventory", true)

    private val groundDistanceValue = FloatValue("GroundDistance", 2F, 0F, 5F)
    private val modeValue = ListValue("Mode", arrayOf("Normal", "Jump", "Port"), "Normal")

    private val msTimer = MSTimer()
    private var potion = -1

    @EventTarget
    fun onMotion(motionEvent: MotionEvent) {
        if (!msTimer.hasTimePassed(delayValue.get().toLong()) || mc.playerController.isInCreativeMode)
            return

        when (motionEvent.eventState) {
            PRE -> {
                // Hotbar Potion
                val potionInHotbar = findPotion(36, 45)

                if (mc.thePlayer.health <= healthValue.get() && potionInHotbar != -1) {
                    if (mc.thePlayer.onGround) {
                        when (modeValue.get().toLowerCase()) {
                            "jump" -> mc.thePlayer.jump()
                            "port" -> mc.thePlayer.moveEntity(0.0, 0.42, 0.0)
                        }
                    }

                    // Prevent throwing potions into the void
                    val fallingPlayer = FallingPlayer(
                            mc.thePlayer.posX,
                            mc.thePlayer.posY,
                            mc.thePlayer.posZ,
                            mc.thePlayer.motionX,
                            mc.thePlayer.motionY,
                            mc.thePlayer.motionZ,
                            mc.thePlayer.rotationYaw,
                            mc.thePlayer.moveStrafing,
                            mc.thePlayer.moveForward
                    )

                    val collisionBlock = fallingPlayer.findCollision(20)?.pos

                    if (mc.thePlayer.posY - (collisionBlock?.y ?: 0) >= groundDistanceValue.get())
                        return

                    potion = potionInHotbar
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange(potion - 36))

                    if (mc.thePlayer.rotationPitch <= 80F) {
                        RotationUtils.setTargetRotation(Rotation(mc.thePlayer.rotationYaw, RandomUtils.nextFloat(80F, 90F)))
                    }
                    return
                }

                // Inventory Potion -> Hotbar Potion
                val potionInInventory = findPotion(9, 36)
                if (potionInInventory != -1 && InventoryUtils.hasSpaceHotbar()) {
                    if (openInventoryValue.get() && mc.currentScreen !is GuiInventory)
                        return

                    val openInventory = mc.currentScreen !is GuiInventory && simulateInventory.get()

                    if (openInventory)
                        mc.netHandler.addToSendQueue(
                                C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))

                    mc.playerController.windowClick(0, potionInInventory, 0, 1, mc.thePlayer)

                    if (openInventory)
                        mc.netHandler.addToSendQueue(C0DPacketCloseWindow())

                    msTimer.reset()
                }
            }
            POST -> {
                if (potion >= 0 && RotationUtils.serverRotation.pitch >= 75F) {
                    val itemStack = mc.thePlayer.inventoryContainer.getSlot(potion).stack

                    if (itemStack != null) {
                        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(itemStack))
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))

                        msTimer.reset()
                    }

                    potion = -1
                }
            }
        }
    }

    private fun findPotion(startSlot: Int, endSlot: Int): Int {
        for (i in startSlot until endSlot) {
            val stack = mc.thePlayer.inventoryContainer.getSlot(i).stack

            if (stack == null || stack.item !is ItemPotion || !ItemPotion.isSplash(stack.itemDamage))
                continue

            val itemPotion = stack.item as ItemPotion

            for (potionEffect in itemPotion.getEffects(stack))
                if (potionEffect.potionID == Potion.heal.id)
                    return i

            if (!mc.thePlayer.isPotionActive(Potion.regeneration))
                for (potionEffect in itemPotion.getEffects(stack))
                    if (potionEffect.potionID == Potion.regeneration.id) return i
        }

        return -1
    }

    override val tag: String?
        get() = healthValue.get().toString()

}