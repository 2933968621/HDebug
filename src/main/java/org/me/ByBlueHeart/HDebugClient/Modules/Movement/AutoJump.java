package org.me.ByBlueHeart.HDebugClient.Modules.Movement;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.MotionEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

@ModuleInfo(name = "AutoJump", description = "Bypass All AntiCheat(May)", category = ModuleCategory.MOVEMENT)
public class AutoJump extends Module {

    @EventTarget
    public void onMotion(MotionEvent e){
        if(mc.thePlayer.isInWater())
            return;
        if(MovementUtils.isMoving()) {
            if(mc.thePlayer.onGround){
                mc.thePlayer.jump();
            }
        }
    }
}
