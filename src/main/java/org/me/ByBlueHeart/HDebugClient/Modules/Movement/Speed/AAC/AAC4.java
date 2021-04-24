package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.AAC;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.Speed;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class AAC4 extends SpeedMode {
    public AAC4() {
        super("AAC4");
    }

    public void onMotion() {
        if (MovementUtils.isMoving()) {
            Speed speed = (Speed) HDebug.moduleManager.getModule(Speed.class);
            if (speed == null) {
                return;
            }
            AAC4.mc.timer.timerSpeed = ((Float) speed.customTimerValue.get()).floatValue();
            if (AAC4.mc.thePlayer.onGround) {
                MovementUtils.strafe((float) 8.0f);
                AAC4.mc.thePlayer.motionY = 0.4;
            } else if (((Boolean) speed.customStrafeValue.get()).booleanValue()) {
                MovementUtils.strafe((float) 8.0f);
            } else {
                MovementUtils.strafe();
            }
        } else {
            AAC4.mc.thePlayer.motionZ = 0.0;
            AAC4.mc.thePlayer.motionX = 0.0;
        }
    }

    public void onEnable() {
        Speed speed = (Speed) HDebug.moduleManager.getModule(Speed.class);
        if (speed == null) {
            return;
        }
        if (((Boolean) speed.resetXZValue.get()).booleanValue()) {
            AAC4.mc.thePlayer.motionZ = 0.0;
            AAC4.mc.thePlayer.motionX = 0.0;
        }
        if (((Boolean) speed.resetYValue.get()).booleanValue()) {
            AAC4.mc.thePlayer.motionY = 0.0;
        }
        super.onEnable();
    }

    public void onDisable() {
        AAC4.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    public void onUpdate() {
    }

    public void onMove(MoveEvent event) {
    }
}