package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.AAC;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class AACLowHop4 extends SpeedMode {
    private boolean legitJump;

    public AACLowHop4() {
        super("AACLowHop4");
    }

    public void onEnable() {
        this.legitJump = true;
        super.onEnable();
    }

    public void onMotion() {
        if (MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) {
                if (this.legitJump) {
                    mc.thePlayer.jump();
                    this.legitJump = false;
                    return;
                }
                mc.thePlayer.motionY = 0.4153999984264374D;
                MovementUtils.strafe(0.0F);
            }
        } else {
            this.legitJump = true;
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
        }
    }

    public void onUpdate() {}

    public void onMove(MoveEvent event) {}
}