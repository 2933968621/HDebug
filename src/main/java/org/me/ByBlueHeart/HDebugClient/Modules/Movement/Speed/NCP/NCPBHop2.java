package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.NCP;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class NCPBHop2 extends SpeedMode {

    public NCPBHop2() {
        super("NCPBHop2");
    }

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1.08F;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @Override
    public void onMotion() {

    }

    @Override
    public void onUpdate() {
        if(MovementUtils.isMoving()) {
            if(mc.thePlayer != null && mc.theWorld != null) {
                if (!mc.thePlayer.isCollidedHorizontally) {
                    mc.gameSettings.keyBindJump.pressed = false;
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionX *= 1.0708F;
                        mc.thePlayer.motionZ *= 1.0708F;
                        mc.thePlayer.moveStrafing *= 2;
                    } else {
                        mc.thePlayer.jumpMovementFactor = 0.0265F;
                    }
                }
            }
            MovementUtils.strafe();
        }else{
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }

    @Override
    public void onMove(MoveEvent event) {

    }
}