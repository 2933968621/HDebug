package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.AAC;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class AACSlowHop extends SpeedMode {
    private boolean legitJump;

    public AACSlowHop() {
        super("AACSlowHop");
    }

    @Override
    public void onEnable() {
        legitJump = true;
        super.onEnable();
    }

    @Override
    public void onMotion() {
        if(MovementUtils.isMoving()) {
            if(mc.thePlayer.onGround) {
                if(legitJump) {
                    mc.thePlayer.jump();
                    legitJump = true;
                    return;
                }
                mc.thePlayer.motionY = 0.42F;
                MovementUtils.strafe(0.0F);
            }
        }else{
            legitJump = true;
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