
package net.blueheart.hdebug.features.module.modules.movement.speeds.aac;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class OldAACBHop extends SpeedMode {

    public OldAACBHop() {
        super("OldAACBHop");
    }

    @Override
    public void onMotion() {
        if(MovementUtils.isMoving()) {
            if(mc.thePlayer.onGround) {
                MovementUtils.strafe(0.56F);
                mc.thePlayer.motionY = 0.41999998688697815;
            }else
                MovementUtils.strafe(MovementUtils.getSpeed() * ((mc.thePlayer.fallDistance > 0.4F) ? 1.0F : 1.01F));
        }else{
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}
