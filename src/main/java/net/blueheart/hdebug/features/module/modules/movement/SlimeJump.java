
package net.blueheart.hdebug.features.module.modules.movement;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.JumpEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.block.BlockUtils;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.block.BlockSlime;

@ModuleInfo(name = "SlimeJump", description = "Allows you to to jump higher on slime blocks.", category = ModuleCategory.MOVEMENT)
public class SlimeJump extends Module {

    private final FloatValue motionValue = new FloatValue("Motion", 0.42F, 0.2F, 1F);
    private final ListValue modeValue = new ListValue("Mode", new String[] {"Set", "Add"}, "Add");

    @EventTarget
    public void onJump(JumpEvent event) {
        if(mc.thePlayer != null && mc.theWorld != null && BlockUtils.getBlock(mc.thePlayer.getPosition().down()) instanceof BlockSlime) {
            event.cancelEvent();

            switch(modeValue.get().toLowerCase()) {
                case "set":
                    mc.thePlayer.motionY = motionValue.get();
                    break;
                case "add":
                    mc.thePlayer.motionY += motionValue.get();
                    break;
            }
        }
    }
}