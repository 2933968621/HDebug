package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Hypixel;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.minecraft.potion.Potion;

public class USHypixel extends SpeedMode {

    public USHypixel() {
        super("USHypixel");
    }

    @Override
    public void onMotion() {
        if(MovementUtils.isMoving()) {
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();

                float speed = MovementUtils.getSpeed() < 0.56F ? MovementUtils.getSpeed() * 1.045F : 0.56F;

                if(mc.thePlayer.onGround && mc.thePlayer.isPotionActive(Potion.moveSpeed))
                    speed *= 1F + 0.13F * (1 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier());
                MovementUtils.strafe(speed);
                return;
            }else if(mc.thePlayer.motionY < 0.2D)
                mc.thePlayer.motionY -= 0.02D;
            MovementUtils.strafe(MovementUtils.getSpeed() * 1.01889F);
        }else{
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0D;
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(final MoveEvent event) {
    }
}