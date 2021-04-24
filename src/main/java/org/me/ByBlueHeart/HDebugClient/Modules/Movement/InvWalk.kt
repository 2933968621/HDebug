package org.me.ByBlueHeart.HDebugClient.Modules.Movement

import net.blueheart.hdebug.event.ClickWindowEvent
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.PacketEvent
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils
import net.blueheart.hdebug.value.BoolValue
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiIngameMenu
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.settings.GameSettings
import net.minecraft.network.play.client.C0BPacketEntityAction
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.network.play.server.S2EPacketCloseWindow

@ModuleInfo(name = "InvWalk", description = "Allows you to walk while an inventory is opened.", category = ModuleCategory.MOVEMENT)
class InvWalk : Module() {

    private val noDetectableValue = BoolValue("NoDetectable", false)
    val aacAdditionProValue = BoolValue("AACAdditionPro", false)
    val matrixValue = BoolValue("Matrix", false)
    private val dontClose = BoolValue("NoClose", true)
    private val noMoveClicksValue = BoolValue("NoMoveClicks", false)

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S2EPacketCloseWindow && dontClose.get() && mc.currentScreen is GuiInventory) {
            event.cancelEvent()

            if (matrixValue.get()) {
                if (event.packet is C0BPacketEntityAction) {
                    val act = event.packet.action
                    if (act == C0BPacketEntityAction.Action.OPEN_INVENTORY) {
                        event.cancelEvent()
                    }
                }
                if (event.packet is C16PacketClientStatus) {
                    val st = event.packet.status
                    if (st == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                        event.cancelEvent()
                    }
                }
            }
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.currentScreen !is GuiChat && mc.currentScreen !is GuiIngameMenu && (!noDetectableValue.get() || mc.currentScreen !is GuiContainer)) {
            mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindForward)
            mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindBack)
            mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindRight)
            mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindLeft)
            mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump)
            mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindSprint)
        }
    }

    @EventTarget
    fun onClick(event: ClickWindowEvent) {
        if (noMoveClicksValue.get() && MovementUtils.isMoving())
            event.cancelEvent()
    }

    override fun onDisable() {
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.currentScreen != null)
            mc.gameSettings.keyBindForward.pressed = false
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindBack) || mc.currentScreen != null)
            mc.gameSettings.keyBindBack.pressed = false
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.currentScreen != null)
            mc.gameSettings.keyBindRight.pressed = false
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.currentScreen != null)
            mc.gameSettings.keyBindLeft.pressed = false
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindJump) || mc.currentScreen != null)
            mc.gameSettings.keyBindJump.pressed = false
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSprint) || mc.currentScreen != null)
            mc.gameSettings.keyBindSprint.pressed = false
    }

    override val tag: String?
        get() = if (aacAdditionProValue.get() || matrixValue.get()) "Basic" else null
}
