
package net.blueheart.hdebug.features.module.modules.movement.speeds.other;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.Speed;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.blueheart.hdebug.utils.timer.MSTimer;

public class TeleportCubeCraft extends SpeedMode {

    private final MSTimer timer = new MSTimer();

    public TeleportCubeCraft() {
        super("TeleportCubeCraft");
    }

    @Override
    public void onMotion() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMove(final MoveEvent event) {
        if(MovementUtils.isMoving() && mc.thePlayer.onGround && timer.hasTimePassed(300L)) {
            final double yaw = MovementUtils.getDirection();
            final float length = ((Speed) HDebug.moduleManager.getModule(Speed.class)).cubecraftPortLengthValue.get();

            event.setX(-Math.sin(yaw) * length);
            event.setZ(Math.cos(yaw) * length);
            timer.reset();
        }
    }
}