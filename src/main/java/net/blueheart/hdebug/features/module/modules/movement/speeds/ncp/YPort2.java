
package net.blueheart.hdebug.features.module.modules.movement.speeds.ncp;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class YPort2 extends SpeedMode {

    public YPort2() {
        super("YPort2");
    }

    @Override
    public void onMotion() {
        if(mc.thePlayer.isOnLadder() || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isInWeb || !MovementUtils.isMoving())
            return;

        if(mc.thePlayer.onGround)
            mc.thePlayer.jump();
        else
            mc.thePlayer.motionY = -1D;

        MovementUtils.strafe();
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(MoveEvent event) {
    }
}
