package org.me.ByBlueHeart.HDebugClient.Modules.Combat;

import net.blueheart.hdebug.event.AttackEvent;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

@ModuleInfo(name = "ArmorBreak", description = "Break Player Armor.", category = ModuleCategory.COMBAT)
public class ArmorBreaker extends Module {

    @EventTarget
    public void onAttack(AttackEvent e){
        if (!mc.thePlayer.onGround) {
            return;
        }
        ItemStack current = mc.thePlayer.getHeldItem();
        for (int i = 0; i < 46; i++) {
            ItemStack toSwitch = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if ((current != null) && (toSwitch != null) && ((toSwitch.getItem() instanceof ItemSword))) {
                mc.playerController.windowClick(0, i, mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);
            }
        }
    }
}
