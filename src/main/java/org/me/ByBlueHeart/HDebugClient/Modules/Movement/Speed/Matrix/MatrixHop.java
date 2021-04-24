package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Matrix;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.Listenable;
import net.blueheart.hdebug.event.MotionEvent;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;

public class MatrixHop extends SpeedMode implements Listenable {
    public MatrixHop() {
        super("MatrixHop");

        HDebug.eventManager.registerListener(this);
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        if(mc.thePlayer.moveForward > 0) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.setSprinting(true);
                mc.timer.timerSpeed = 0.775F;
                mc.thePlayer.motionX *= 1.08;
                mc.thePlayer.motionZ *= 1.08;
            }else if(mc.thePlayer.fallDistance > 0){
                mc.timer.timerSpeed = 2.5F;
            }
        }
    }

    @Override
    public void onMove(final MoveEvent event) {
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
    }

    @Override
    public void onEnable() {
        if(mc.thePlayer.onGround)
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
    }

    @Override
    public void onDisable() {
        mc.thePlayer.jumpMovementFactor = 0.02F;
    }

    @Override
    public boolean handleEvents() {
        return isActive();
    }
}