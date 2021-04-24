package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.AAC;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.Listenable;
import net.blueheart.hdebug.event.MotionEvent;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;

public class AAC4FastHop extends SpeedMode implements Listenable {
    public AAC4FastHop() {
        super("AAC4FastHop");
        HDebug.eventManager.registerListener(this);
    }

    public void onMotion() {}

    public void onUpdate() {
        if (mc.thePlayer.moveForward > 0.0F)
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.setSprinting(true);
                mc.timer.timerSpeed = 0.675F;
                mc.thePlayer.motionX *= 1.08D;
                mc.thePlayer.motionZ *= 1.08D;
            } else if (mc.thePlayer.lastTickPosX > 0.0F) {
                mc.timer.timerSpeed = 2.4F;
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