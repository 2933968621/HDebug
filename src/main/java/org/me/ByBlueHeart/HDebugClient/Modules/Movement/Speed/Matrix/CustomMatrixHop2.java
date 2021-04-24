package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Matrix;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.Listenable;
import net.blueheart.hdebug.event.MotionEvent;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.Speed;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;

public class CustomMatrixHop2 extends SpeedMode implements Listenable {
    public CustomMatrixHop2() {
        super("CustomMatrixHop2");
        HDebug.eventManager.registerListener(this);
    }

    public void onMotion() {}

    public void onUpdate() {
        if (mc.thePlayer.moveForward > 0.0F)
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.setSprinting(true);
                mc.timer.timerSpeed = ((Float)((Speed)HDebug.moduleManager.getModule(Speed.class)).custommatrixHop2TimerValue2.get()).floatValue();
                mc.thePlayer.motionX *= 1.0800000429153442D;
                mc.thePlayer.motionZ *= 1.0800000429153442D;
            } else if (mc.thePlayer.lastTickPosX > 0.0F) {
                mc.timer.timerSpeed = ((Float)((Speed)HDebug.moduleManager.getModule(Speed.class)).custommatrixHop2TimerValue.get()).floatValue();
            }
    }

    public void onMove(MoveEvent event) {}

    @EventTarget
    public void onMotion(MotionEvent event) {}

    public void onEnable() {
        if (mc.thePlayer.onGround)
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0.0D;
    }

    public void onDisable() {
        mc.thePlayer.jumpMovementFactor = 0.02F;
    }

    public boolean handleEvents() {
        return isActive();
    }
}