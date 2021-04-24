package org.me.ByBlueHeart.HDebugClient.Modules.Combat

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.utils.InventoryUtils
import net.blueheart.hdebug.value.IntegerValue
import net.minecraft.init.Items
import net.minecraft.network.play.client.C09PacketHeldItemChange

@ModuleInfo(name = "AutoGApple", description = "Makes you automatically eat golden apple whenever your health is low.", category = ModuleCategory.COMBAT)
class AutoGApple : Module() {

    private val healthValue = IntegerValue("Health", 10, 0, 19)

    override val tag: String
        get() = healthValue.get().toString()

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if(mc.thePlayer.getHealth() <= healthValue.get()) {
            mc.thePlayer.sendQueue.addToSendQueue(C09PacketHeldItemChange(InventoryUtils.findItem(1, 45, Items.golden_apple)));
            mc.gameSettings.keyBindUseItem.pressed = true
        }
    }
}