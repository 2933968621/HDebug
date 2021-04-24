package net.blueheart.hdebug.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class RotationUtil {
    public static float[] getRotations(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Helper.mc.thePlayer.posX;
        double diffZ = entity.posZ - Helper.mc.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + ((double)elb.getEyeHeight() - 0.4) - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        } else {
            diffY = (0.1 + 0.45) / 2.0 - (Helper.mc.thePlayer.posY + (double)Helper.mc.thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }
    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }
}