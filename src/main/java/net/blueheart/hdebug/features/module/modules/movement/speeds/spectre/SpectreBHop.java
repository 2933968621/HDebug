
package net.blueheart.hdebug.features.module.modules.movement.speeds.spectre;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class SpectreBHop extends SpeedMode {

    public SpectreBHop() {
        super("SpectreBHop");
    }

    @Override
    public void onMotion() {
        if(!MovementUtils.isMoving() || mc.thePlayer.movementInput.jump)
            return;

        if(mc.thePlayer.onGround) {
            MovementUtils.strafe(1.1F);
            mc.thePlayer.motionY = 0.44D;
            return;
        }

        MovementUtils.strafe();
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}
