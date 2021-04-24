
package net.blueheart.hdebug.features.module.modules.movement.speeds.aac;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.Speed;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AACGround extends SpeedMode {
    public AACGround() {
        super("AACGround");
    }

    @Override
    public void onMotion() {

    }

    @Override
    public void onUpdate() {
        if(!MovementUtils.isMoving())
            return;

        mc.timer.timerSpeed = ((Speed) HDebug.moduleManager.getModule(Speed.class)).aacGroundTimerValue.get();
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }

    @Override
    public void onMove(MoveEvent event) {

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
    }
}
