package org.me.ByBlueHeart.HDebugClient.Modules.Movement;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.features.module.modules.movement.Speed;
import net.blueheart.hdebug.utils.RotationUtils;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.util.AxisAlignedBB;
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.Aura;

@ModuleInfo(name = "TargetStrafe", description = "TargetStrafe.", category = ModuleCategory.PLAYER)
public class TargetStrafe extends Module {
    public final ListValue modeValue = new ListValue("KeyMode", new String[] { "Jump", "None" }, "Jump");

    private final FloatValue radiusValue = new FloatValue("radius", 0.1F, 0.1F, 4.0F);

    final Aura killAura = (Aura)HDebug.moduleManager.getModule(Aura.class);

    int consts;

    double lastDist;

    @EventTarget
    public void movestrafe(MoveEvent event) {
        Speed speed = (Speed)HDebug.moduleManager.getModule(Speed.class);
        double xDist = event.getX();
        double zDist = event.getZ();
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist) * 0.995D;
        if (canStrafe()) {
            float[] Baics = RotationUtils.getRotations(this.killAura.getTarget());
            setSpeed(event, this.lastDist, Baics[0], ((Float)this.radiusValue.get()).floatValue(), 1.0D);
        }
    }

    public boolean keyMode() {
        boolean strafe = (mc.thePlayer.movementInput.moveStrafe != 0.0F || mc.thePlayer.movementInput.moveForward != 0.0F);
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "jump":
                return (mc.gameSettings.keyBindJump.isKeyDown() && strafe);
            case "none":
                return strafe;
        }
        return false;
    }

    public boolean canStrafe() {
        Speed speed = (Speed)HDebug.moduleManager.getModule(Speed.class);
        Flight fly = (Flight)HDebug.moduleManager.getModule(Flight.class);
        boolean cheak = (speed.getState() || fly.getState());
        return (this.killAura.getState() && cheak && this.killAura.getTarget() != null && !mc.thePlayer.isSneaking() &&
                keyMode());
    }

    public void setSpeed(MoveEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        float yaw = pseudoYaw;
        double Enemydistance = mc.thePlayer.getDistance((this.killAura.getTarget()).posX, mc.thePlayer.posY, (this.killAura.getTarget()).posZ);
        double radius = ((Float)this.radiusValue.get()).floatValue();
        float D = (float)Math.max(Enemydistance - radius, Enemydistance - Enemydistance - radius / radius * 2.0D);
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        if (mc.thePlayer.isCollidedHorizontally || checkVoid())
            if (this.consts < 2) {
                this.consts++;
            } else {
                this.consts = -1;
            }
        switch (this.consts) {
            case 0:
                this.consts = 1;
                break;
            case 2:
                this.consts = -1;
                break;
        }
        switch (this.modeValue.get().toLowerCase()) {
            case "jump":
                strafe = pseudoStrafe * mc.thePlayer.movementInput.moveStrafe * this.consts;
                break;
            case "none":
                strafe = this.consts;
                break;
        }
        float str = (strafe > 0.0D && forward != 0.0D) ? ((forward > 0.0D) ? (-45.0F / D) : (45.0F / D)) : ((strafe < 0.0D && forward != 0.0D) ? ((forward > 0.0D) ? (45.0F / D) : (-45.0F / D)) : 0.0F);
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += (forward > 0.0D) ? (-45.0F / D) : (45.0F / D);
            } else if (strafe < 0.0D) {
                yaw += (forward > 0.0D) ? (45.0F / D) : (-45.0F / D);
            }
            strafe = 0.0D;
            if (forward > 0.0D || D <= 0.0F) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0D + str / 4.5D));
        double mz = Math.sin(Math.toRadians(yaw + 90.0D + str / 4.5D));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }

    private boolean checkVoid() {
        for (int x = -1; x < 1; x++) {
            for (int z = -1; z < 1; z++) {
                if (isVoid(x, z))
                    return true;
            }
        }
        return false;
    }

    private boolean isVoid(int X, int Z) {
        Flight fly = (Flight) HDebug.moduleManager.getModule(Flight.class);
        if (fly.getState())
            return false;
        if (mc.thePlayer.posY < 0.0D)
            return true;
        for (int off = 0; off < (int)mc.thePlayer.posY + 2; ) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(X, -off, Z);
            if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                off += 2;
                continue;
            }
            return false;
        }
        return true;
    }
}