package org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Hypixel;

import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import net.blueheart.hdebug.utils.MinecraftInstance;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;

public class HypixelHop extends SpeedMode {
    private int stage;
    private double movementSpeed;
    private double distance;

    public HypixelHop() {
        super("HypixelHop");
    }

    public void onEnable() {
        MinecraftInstance.mc.timer.timerSpeed = 1.0F;
        this.stage = 2;
        this.stage = MinecraftInstance.mc.theWorld.getCollidingBoundingBoxes(MinecraftInstance.mc.thePlayer, MinecraftInstance.mc.thePlayer.getEntityBoundingBox().offset(0.0D, MinecraftInstance.mc.thePlayer.motionY, 0.0D)).size() <= 0 && !MinecraftInstance.mc.thePlayer.isCollidedVertically?4:1;
    }

    public void onDisable() {
        MinecraftInstance.mc.timer.timerSpeed = 1.0F;
    }

    public void onUpdate() {
    }

    public void onMotion() {
        double xDist = MinecraftInstance.mc.thePlayer.posX - MinecraftInstance.mc.thePlayer.prevPosX;
        double zDist = MinecraftInstance.mc.thePlayer.posZ - MinecraftInstance.mc.thePlayer.prevPosZ;
        this.distance = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public boolean canZoom() {
        return (MinecraftInstance.mc.thePlayer.moveForward != 0.0F || MinecraftInstance.mc.thePlayer.moveStrafing != 0.0F) && MinecraftInstance.mc.thePlayer.onGround;
    }

    private double getBaseMovementSpeed() {
        double baseSpeed = 0.2873D;
        if(MinecraftInstance.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0D + 0.2D * (double)(MinecraftInstance.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return baseSpeed;
    }

    public void onMove(MoveEvent e) {
        switch(this.stage) {
            case 1:
                if(this.canZoom()) {
                    this.movementSpeed = this.getBaseMovementSpeed();
                }
                break;
            case 2:
                double movementInput = 0.40747D;
                if(MinecraftInstance.mc.thePlayer.isPotionActive(Potion.jump)) {
                    movementInput = 0.40747D + (double)(MinecraftInstance.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1D;
                    MinecraftInstance.mc.thePlayer.jump();
                }

                if(!this.canZoom()) {
                    return;
                }

                MinecraftInstance.mc.thePlayer.motionY = movementInput;
                e.setY(movementInput);
                this.movementSpeed *= this.getHypixelSpeed();
                break;
            case 3:
                this.movementSpeed = this.distance - 0.7225D * (this.distance - this.getBaseMovementSpeed());
                break;
            default:
                if(MinecraftInstance.mc.theWorld.getCollidingBoundingBoxes(MinecraftInstance.mc.thePlayer, MinecraftInstance.mc.thePlayer.getEntityBoundingBox().offset(0.0D, MinecraftInstance.mc.thePlayer.motionY, 0.0D)).size() > 0 || MinecraftInstance.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                    this.stage = MinecraftInstance.mc.thePlayer.moveForward == 0.0F && MinecraftInstance.mc.thePlayer.moveStrafing == 0.0F?0:1;
                }

                this.movementSpeed = this.distance - this.distance / 159.0D;
        }

        this.movementSpeed = Math.max(this.movementSpeed, this.getBaseMovementSpeed());
        MovementInput var10 = MinecraftInstance.mc.thePlayer.movementInput;
        float forward = var10.moveForward;
        float strafe = var10.moveStrafe;
        float yaw = MinecraftInstance.mc.thePlayer.rotationYaw;
        if(forward == 0.0F && strafe == 0.0F) {
            e.setX(0.0D);
            e.setZ(0.0D);
        } else if(forward != 0.0F) {
            if(strafe >= 1.0F) {
                yaw += (float)(forward > 0.0F?-45:45);
                strafe = 0.0F;
            } else if(strafe <= -1.0F) {
                yaw += (float)(forward > 0.0F?45:-45);
                strafe = 0.0F;
            }

            if(forward > 0.0F) {
                forward = 1.0F;
            } else if(forward < 0.0F) {
                forward = -1.0F;
            }
        }

        double mx2 = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
        double mz2 = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
        e.setX(((double)forward * this.movementSpeed * mx2 + (double)strafe * this.movementSpeed * mz2) * 0.99D);
        e.setZ(((double)forward * this.movementSpeed * mz2 - (double)strafe * this.movementSpeed * mx2) * 0.99D);
        if(MinecraftInstance.mc.thePlayer.moveForward != 0.0F || MinecraftInstance.mc.thePlayer.moveStrafing != 0.0F) {
            ++this.stage;
        }

    }

    private double getHypixelSpeed() {
        boolean isOnStaris = MinecraftInstance.mc.theWorld.getBlockState(new BlockPos(MinecraftInstance.mc.thePlayer.posX, MinecraftInstance.mc.thePlayer.posY - 1.0D, MinecraftInstance.mc.thePlayer.posZ)).getBlock() instanceof BlockStairs;
        boolean isOnSlab = MinecraftInstance.mc.theWorld.getBlockState(new BlockPos(MinecraftInstance.mc.thePlayer.posX, MinecraftInstance.mc.thePlayer.posY - 0.1D, MinecraftInstance.mc.thePlayer.posZ)).getBlock() instanceof BlockSlab && Math.floor(MinecraftInstance.mc.thePlayer.posY) != MinecraftInstance.mc.thePlayer.posY;
        double moveSpeed;
        if(isOnStaris) {
            moveSpeed = 1.255D;
        } else if(isOnSlab) {
            moveSpeed = 1.322D;
        } else if(MinecraftInstance.mc.thePlayer.isInLava()) {
            moveSpeed = 1.0D;
        } else if(MinecraftInstance.mc.thePlayer.isInWater()) {
            moveSpeed = 1.255D;
        } else {
            moveSpeed = 2.149D;
        }

        return moveSpeed;
    }
}
