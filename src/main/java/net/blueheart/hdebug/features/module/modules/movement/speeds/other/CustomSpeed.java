
package net.blueheart.hdebug.features.module.modules.movement.speeds.other;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.Speed;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

public class CustomSpeed extends SpeedMode {

    public CustomSpeed() {
        super("Custom");
    }

    @Override
    public void onMotion() {
        if(MovementUtils.isMoving()) {
            final Speed speed = (Speed) HDebug.moduleManager.getModule(Speed.class);

            if(speed == null)
                return;

            mc.timer.timerSpeed = speed.customTimerValue.get();

            if(mc.thePlayer.onGround) {
                MovementUtils.strafe(speed.customSpeedValue.get());
                mc.thePlayer.motionY = speed.customYValue.get();
            }else if(speed.customStrafeValue.get())
                MovementUtils.strafe(speed.customSpeedValue.get());
            else
                MovementUtils.strafe();
        }else
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0D;
    }

    @Override
    public void onEnable() {
        final Speed speed = (Speed) HDebug.moduleManager.getModule(Speed.class);

        if(speed == null)
            return;

        if(speed.resetXZValue.get()) mc.thePlayer.motionX = mc.thePlayer.motionZ = 0D;
        if(speed.resetYValue.get()) mc.thePlayer.motionY = 0D;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(MoveEvent event) {
    }
}
