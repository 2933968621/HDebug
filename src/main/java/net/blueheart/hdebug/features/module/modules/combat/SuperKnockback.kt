
package net.blueheart.hdebug.features.module.modules.combat

import net.blueheart.hdebug.event.AttackEvent
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.IntegerValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C0BPacketEntityAction

@ModuleInfo(name = "SuperKnockback", description = "Increases knockback dealt to other entities.", category = ModuleCategory.COMBAT)
class SuperKnockback : Module() {
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityLivingBase) {
            if (event.targetEntity.hurtTime > hurtTimeValue.get())
                return

            if (mc.thePlayer.isSprinting)
                mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING))

            mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING))
            mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING))
            mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING))
            mc.thePlayer.isSprinting = true
            mc.thePlayer.serverSprintState = true
        }
    }
}