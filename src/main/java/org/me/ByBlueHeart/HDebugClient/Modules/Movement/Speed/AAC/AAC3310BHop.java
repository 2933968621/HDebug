package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.AAC;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class AAC3310BHop extends SpeedMode {
    public AAC3310BHop() {
        super("AAC3310BHop");
    }

    public void onMotion() {
        if (!MovementUtils.isMoving() || mc.thePlayer.movementInput.jump)
            return;
        if (mc.thePlayer.onGround) {
            MovementUtils.strafe(1.3F);
            mc.thePlayer.motionY = 0.45D;
            return;
        }
        MovementUtils.strafe();
    }

    public void onUpdate() {}

    public void onMove(MoveEvent event) {}
}