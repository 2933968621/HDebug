
package net.blueheart.hdebug.features.module.modules.movement.speeds.other;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class SlowHop extends SpeedMode {

    public SlowHop() {
        super("SlowHop");
    }

    @Override
    public void onMotion() {
        if(mc.thePlayer.isInWater())
            return;

        if(MovementUtils.isMoving()) {
            if(mc.thePlayer.onGround)
                mc.thePlayer.jump();
            else
                MovementUtils.strafe(MovementUtils.getSpeed() * 1.011F);
        }else{
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(MoveEvent event) {
    }
}
