package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Other;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import net.blueheart.hdebug.utils.MinecraftInstance;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.minecraft.potion.Potion;

import java.util.List;

public class BHop extends SpeedMode {
    private int stage;
    private double movementSpeed;
    private double distance;

    public BHop() {
        super("BHop");
    }

    public void onEnable() {
        MinecraftInstance.mc.timer.timerSpeed = 1.0F;
        this.stage = 0;
    }

    public void onDisable() {
        MinecraftInstance.mc.timer.timerSpeed = 1.0F;
    }

    public void onMotion() {
        double xDist = MinecraftInstance.mc.thePlayer.posX - MinecraftInstance.mc.thePlayer.prevPosX;
        double zDist = MinecraftInstance.mc.thePlayer.posZ - MinecraftInstance.mc.thePlayer.prevPosZ;
        this.distance = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public void onUpdate() {
    }

    public boolean canZoom() {
        return (MinecraftInstance.mc.thePlayer.moveForward != 0.0F || MinecraftInstance.mc.thePlayer.moveStrafing != 0.0F) && MinecraftInstance.mc.thePlayer.onGround;
    }

    private static double randomD(double min, double max, int scl) {
        int pow = (int)Math.pow(10.0D, (double)scl);
        return Math.floor((Math.random() * (max - min) + min) * (double)pow) / (double)pow;
    }

    List getCollidingList(double motionY) {
        return MinecraftInstance.mc.theWorld.getCollidingBoundingBoxes(MinecraftInstance.mc.thePlayer, MinecraftInstance.mc.thePlayer.getEntityBoundingBox().offset(0.0D, motionY, 0.0D));
    }

    private double defaultSpeed() {
        double baseSpeed = 0.2873D;
        if(MinecraftInstance.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = MinecraftInstance.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
        }

        return baseSpeed;
    }

    private void setMotion(MoveEvent em, double speed) {
        double forward = (double)MinecraftInstance.mc.thePlayer.movementInput.moveForward;
        double strafe = (double)MinecraftInstance.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MinecraftInstance.mc.thePlayer.rotationYaw;
        if(forward == 0.0D && strafe == 0.0D) {
            em.setX(0.0D);
            em.setZ(0.0D);
        } else {
            if(forward != 0.0D) {
                if(strafe > 0.0D) {
                    yaw += (float)(forward > 0.0D?-45:45);
                } else if(strafe < 0.0D) {
                    yaw += (float)(forward > 0.0D?45:-45);
                }

                strafe = 0.0D;
                if(forward > 0.0D) {
                    forward = 1.0D;
                } else if(forward < 0.0D) {
                    forward = -1.0D;
                }
            }

            em.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
            em.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
        }

    }

    public void onMove(MoveEvent e) {
        if(this.stage == 1) {
            ++this.stage;
        }

        double arithmo = 0.408712159634D;
        if(MinecraftInstance.mc.thePlayer.isPotionActive(Potion.jump)) {
            arithmo += (double)((float)(MinecraftInstance.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
        }

        if(this.canZoom() && this.stage == 2 && (MinecraftInstance.mc.thePlayer.moveForward != 0.0F || MinecraftInstance.mc.thePlayer.moveStrafing != 0.0F)) {
            MinecraftInstance.mc.thePlayer.motionY = arithmo;
            e.setY(MinecraftInstance.mc.thePlayer.motionY);
            this.movementSpeed *= 2.0D;
        } else if(this.stage == 3) {
            double diff = (0.66D + this.movementSpeed * 0.07D) * (this.distance - this.defaultSpeed());
            this.movementSpeed = this.distance - diff;
        } else {
            if(this.getCollidingList(e.getY()).size() > 0 || MinecraftInstance.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                this.stage = MovementUtils.isMoving()?1:0;
            }

            this.movementSpeed = this.distance - this.distance / 159.21D;
        }

        this.movementSpeed = Math.max(this.movementSpeed, this.defaultSpeed());
        this.setMotion(e, this.movementSpeed);
        ++this.stage;
    }
}
