package org.me.ByBlueHeart.HDebugClient.Modules.Movement;

import net.blueheart.hdebug.event.*;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.ClientUtils;
import net.blueheart.hdebug.utils.render.RenderUtils;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.blueheart.hdebug.utils.timer.TickTimer;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.IntegerValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@ModuleInfo(name = "Flight", description = "Allows you to fly in survival mode.", category = ModuleCategory.MOVEMENT, keyBind = Keyboard.KEY_F)
public class Flight extends Module {

    public final ListValue modeValue = new ListValue("Mode", new String[]{
            // Vanilla
            "Vanilla",
            "SmoothVanilla",
            "GlideVanilla",
            "Motion",

            // NCP
            "NCP",
            "OldNCP",

            // AAC
            "AAC1.9.10",
            "AAC3.0.5",
            "AAC3.1.6-Gomme",
            "AAC3.3.12",
            "AAC3.3.12-Glide",
            "AAC3.3.13",
            "AAC3.3.14-Rewi",

            // CubeCraft
            "CubeCraft",
            "CubeCraft2",
            "InfiniteCubeCraft",

            // Hypixel
            "Hypixel",
            "BoostHypixel",
            "FreeHypixel",

            // Rewinside
            "Rewinside",
            "TeleportRewinside",

            // LibreCraft
            "LibreCraft",
            "LibreCraft2",
            "LibreCraft3",

            // Other server specific flys
            "Mineplex",
            "NeruxVace",
            "Minesucht",
            "Matrix-Glide",

            // Spartan
            "Spartan",
            "Spartan2",
            "BugSpartan",

            // Other anticheats
            "MineSecure",
            "HawkEye",
            "HAC",
            "WatchCat",

            // Other
            "Jetpack",
            "KeepAlive",
            "Flag"
    }, "Vanilla");

    // Other
    private final FloatValue vanillaSpeedValue = new FloatValue("VanillaSpeed", 2F, 0F, 5F);
    private final BoolValue vanillaKickBypassValue = new BoolValue("VanillaKickBypass", false);
    private final FloatValue motionSpeedValue = new FloatValue("MotionSpeed", 2.0F, 0.0F, 5.0F);
    private final FloatValue ncpMotionValue = new FloatValue("NCPMotion", 0F, 0F, 1F);

    // AAC
    private final FloatValue aacSpeedValue = new FloatValue("AAC1.9.10-Speed", 0.3F, 0F, 1F);
    private final BoolValue aacFast = new BoolValue("AAC3.0.5-Fast", true);
    private final FloatValue aacMotion = new FloatValue("AAC3.3.12-Motion", 10F, 0.1F, 10F);
    private final FloatValue aacMotion2 = new FloatValue("AAC3.3.13-Motion", 10F, 0.1F, 10F);

    // Hypixel
    private final BoolValue uhc = new BoolValue("HypixelUHC", false);
    private final BoolValue hypixelBoost = new BoolValue("Hypixel-Boost", true);
    private final IntegerValue hypixelBoostDelay = new IntegerValue("Hypixel-BoostDelay", 1200, 0, 2000);
    private final FloatValue hypixelBoostTimer = new FloatValue("Hypixel-BoostTimer", 1F, 0F, 5F);

    //Minepixel
    private final FloatValue mineplexSpeedValue = new FloatValue("MineplexSpeed", 1F, 0.5F, 10F);
    private final IntegerValue neruxVaceTicks = new IntegerValue("NeruxVace-Ticks", 6, 0, 20);

    //MoveCheck
    private final BoolValue lagback = new BoolValue("LagBackCheck",true);

    // Visuals
    private final BoolValue markValue = new BoolValue("Mark", true);
    private final BoolValue headValue = new BoolValue("HeadMark", true);
    private final BoolValue BobValue = new BoolValue("Bobbing", true);

    private double startY;
    private final MSTimer flyTimer = new MSTimer();

    private final MSTimer groundTimer = new MSTimer();

    private boolean noPacketModify;

    private double aacJump;

    private int aac3delay;
    private int aac3glideDelay;

    private boolean noFlag;

    private final MSTimer mineSecureVClipTimer = new MSTimer();

    private final TickTimer spartanTimer = new TickTimer();

    private long minesuchtTP;

    private final MSTimer mineplexTimer = new MSTimer();

    private boolean wasDead;

    private final TickTimer hypixelTimer = new TickTimer();

    public static double hypixel = 0.0D;
    int counter;
    private double y = 0.0;
    private int boostHypixelState = 1;
    private double moveSpeed, lastDistance;
    private boolean failedStart = false;
    // CubeCraft
    private final TickTimer cubecraftTeleportTickTimer2 = new TickTimer();
    private final TickTimer cubecraftTeleportTickTimer3 = new TickTimer();
    private final TickTimer cubecraftTeleportTickTimer = new TickTimer();

    private final TickTimer freeHypixelTimer = new TickTimer();
    private float freeHypixelYaw;
    private float freeHypixelPitch;

    @Override
    public void onEnable() {
        if(mc.thePlayer == null)
            return;

        flyTimer.reset();

        noPacketModify = true;
        y = 0.0;
        counter = 0;
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        final String mode = modeValue.get();

        switch(mode.toLowerCase()) {
            case "ncp":
                if (!mc.thePlayer.onGround)
                    break;

                for (int i = 0; i < 65; ++i) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049D, z, false));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1D, z, true));

                mc.thePlayer.motionX *= 0.1D;
                mc.thePlayer.motionZ *= 0.1D;
                mc.thePlayer.swingItem();
                break;
            case "oldncp":
                if (!mc.thePlayer.onGround)
                    break;

                for (int i = 0; i < 4; i++) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.01, z, false));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                mc.thePlayer.jump();
                mc.thePlayer.swingItem();
                break;
            case "bugspartan":
                for (int i = 0; i < 65; ++i) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049D, z, false));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1D, z, true));

                mc.thePlayer.motionX *= 0.1D;
                mc.thePlayer.motionZ *= 0.1D;
                mc.thePlayer.swingItem();
                break;
            case "boosthypixel":
                PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                playerCapabilities.allowFlying = true;
                playerCapabilities.isFlying = true;
                mc.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
                damage();
                mc.thePlayer.jump();
                y += 0.41999998688697815D;
                this.boostHypixelState = 1;
                this.moveSpeed = 0.1D;
                this.lastDistance = 0.0D;
                this.failedStart = false;
                break;
        }

        startY = mc.thePlayer.posY;
        aacJump = -3.8D;
        noPacketModify = false;

        if(mode.equalsIgnoreCase("freehypixel")) {
            freeHypixelTimer.reset();
            mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ);
            freeHypixelYaw = mc.thePlayer.rotationYaw;
            freeHypixelPitch = mc.thePlayer.rotationPitch;
        }
        super.onEnable();
    }
    public static void damage() {
        NetHandlerPlayClient netHandler = mc.getNetHandler();
        EntityPlayerSP player = mc.thePlayer;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        int i = 0;
        while ((double) i < (double) getMaxFallDist() / 0.0555) {
            netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double) 0.0601f, z, false));
            netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double) 0.0055f + 0.0000000601, z, false));
            ++i;
        }
        netHandler.addToSendQueue(new C03PacketPlayer(true));
    }
    public static int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump))
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        else
            return 0;
    }
    public static int getSpeedEffect() {
        return mc.thePlayer.isPotionActive(Potion.moveSpeed)?mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1:0;
    }
    public static void setMotion(double var0) {
        double var2 = mc.thePlayer.movementInput.moveForward;
        double var4 = mc.thePlayer.movementInput.moveStrafe;
        float var6 = mc.thePlayer.rotationYaw;
        if(var2 == 0.0D && var4 == 0.0D) {
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
        } else {
            if(var2 != 0.0D) {
                if(var4 > 0.0D) {
                    var6 += (float)(var2 > 0.0D?-45:45);
                } else if(var4 < 0.0D) {
                    var6 += (float)(var2 > 0.0D?45:-45);
                }

                var4 = 0.0D;
                if(var2 > 0.0D) {
                    var2 = 1.0D;
                } else if(var2 < 0.0D) {
                    var2 = -1.0D;
                }
            }

            mc.thePlayer.motionX = var2 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F))) + var4 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F)));
            mc.thePlayer.motionZ = var2 * var0 * Math.sin(Math.toRadians((double)(var6 + 90.0F))) - var4 * var0 * Math.cos(Math.toRadians((double)(var6 + 90.0F)));
        }

    }
    @Override
    public void onDisable() {

        wasDead = false;

        if (mc.thePlayer == null)
            return;

        noFlag = false;

        final String mode = modeValue.get();

        if (!mode.toUpperCase().startsWith("AAC") &&
                !mode.equalsIgnoreCase("Hypixel") &&
                !mode.equalsIgnoreCase("CubeCraft") &&
                !mode.equalsIgnoreCase("CubeCraft2") &&
                !mode.equalsIgnoreCase("InfiniteCubeCraft")) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionY = 0;
            mc.thePlayer.motionZ = 0;
        }
        mc.thePlayer.capabilities.isFlying = false;

        mc.timer.timerSpeed = 1F;
        mc.thePlayer.speedInAir = 0.02F;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (amplifier + 1);
        }
        return baseSpeed;
    }

    @EventTarget
    public void onUpdate2(final UpdateEvent event) {
        Vec3 vectorStart3;
        float yaw3, pitch3;
        Vec3 vectorEnd3, vectorStart4;
        float yaw4, pitch4;
        Vec3 vectorEnd4, vectorStart5;
        float yaw5, pitch5;
        Vec3 vectorEnd5, vectorStart2;
        float yaw2, pitch2;
        Vec3 vectorEnd2;
        if(BobValue.get())
            mc.thePlayer.cameraYaw = 0.1F;
        final float vanillaSpeed = vanillaSpeedValue.get();
        double speed = Math.max(motionSpeedValue.get(), getBaseMoveSpeed());
        switch (modeValue.get().toLowerCase()) {
            case "glidevanilla":
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = -0.2D;
                mc.thePlayer.motionX = 0.0D;
                mc.thePlayer.motionZ = 0.0D;
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.thePlayer.motionY += vanillaSpeed;
                if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY -= vanillaSpeed;
                MovementUtils.strafe(vanillaSpeed);
                break;
            case "infinitecubecraft":
                mc.timer.timerSpeed = 0.3F;
                mc.thePlayer.motionY = (mc.thePlayer.motionY <= -0.42D) ? 0.42D : -0.42D;
                this.cubecraftTeleportTickTimer3.update();
            case "cubecraft2":
                mc.timer.timerSpeed = 0.4F;
                this.cubecraftTeleportTickTimer2.update();
                break;
            case "motion":
                mc.thePlayer.onGround = false;
                if (mc.thePlayer.movementInput.jump) {
                    mc.thePlayer.motionY = speed * 0.6D;
                    break;
                }
                if (mc.thePlayer.movementInput.sneak) {
                    mc.thePlayer.motionY = -speed * 0.6D;
                    break;
                }
                mc.thePlayer.motionY = 0.0D;
                break;
            case "vanilla":
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.thePlayer.motionY += vanillaSpeed;
                if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY -= vanillaSpeed;
                MovementUtils.strafe(vanillaSpeed);

                handleVanillaKickBypass();
                break;
            case "smoothvanilla":
                mc.thePlayer.capabilities.isFlying = true;

                handleVanillaKickBypass();
                break;
            case "cubecraft":
                mc.timer.timerSpeed = 0.6F;

                cubecraftTeleportTickTimer.update();
                break;
            case "ncp":
                mc.thePlayer.motionY = -ncpMotionValue.get();

                if(mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY = -0.5D;
                MovementUtils.strafe();
                break;
            case "oldncp":
                if(startY > mc.thePlayer.posY)
                    mc.thePlayer.motionY = -0.000000000000000000000000000000001D;

                if(mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY = -0.2D;

                if(mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.posY < (startY - 0.1D))
                    mc.thePlayer.motionY = 0.2D;
                MovementUtils.strafe();
                break;
            case "aac1.9.10":
                if(mc.gameSettings.keyBindJump.isKeyDown())
                    aacJump += 0.2D;

                if(mc.gameSettings.keyBindSneak.isKeyDown())
                    aacJump -= 0.2D;

                if((startY + aacJump) > mc.thePlayer.posY) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    mc.thePlayer.motionY = 0.8D;
                    MovementUtils.strafe(aacSpeedValue.get());
                }

                MovementUtils.strafe();
                break;
            case "aac3.0.5":
                if (aac3delay == 2)
                    mc.thePlayer.motionY = 0.1D;
                else if (aac3delay > 2)
                    aac3delay = 0;

                if (aacFast.get()) {
                    if (mc.thePlayer.movementInput.moveStrafe == 0D)
                        mc.thePlayer.jumpMovementFactor = 0.08F;
                    else
                        mc.thePlayer.jumpMovementFactor = 0F;
                }

                aac3delay++;
                break;
            case "matrix-glide":
                if(mc.thePlayer.onGround) {
                    mc.timer.timerSpeed = 1.0F;
                } else {
                    if (mc.thePlayer.fallDistance > 3.0F) {
                        mc.thePlayer.motionY = 0.0F;
                        mc.thePlayer.onGround = false;
                        mc.thePlayer.fallDistance = 0.0F;

                        mc.timer.timerSpeed = 0.79F;
                    }
                }
                break;
            case "aac3.1.6-gomme":
                mc.thePlayer.capabilities.isFlying = true;

                if (aac3delay == 2) {
                    mc.thePlayer.motionY += 0.05D;
                } else if (aac3delay > 2) {
                    mc.thePlayer.motionY -= 0.05D;
                    aac3delay = 0;
                }

                aac3delay++;

                if(!noFlag)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
                if(mc.thePlayer.posY <= 0D)
                    noFlag = true;
                break;
            case "aac3.3.14-rewi":
                vectorStart3 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                yaw3 = -mc.thePlayer.rotationYaw;
                pitch3 = -mc.thePlayer.rotationPitch;
                vectorEnd3 = new Vec3(Math.sin(Math.toRadians(yaw3)) * Math.cos(Math.toRadians(pitch3)) * 6.9D + vectorStart3.xCoord, Math.sin(Math.toRadians(pitch3)) * 6.9D + vectorStart3.yCoord, Math.cos(Math.toRadians(yaw3)) * Math.cos(Math.toRadians(pitch3)) * 6.9D + vectorStart3.zCoord);
                mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorEnd3.xCoord, mc.thePlayer.posY + 0.6D, vectorEnd3.zCoord, true));
                mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorStart3.xCoord, mc.thePlayer.posY + 0.6D, vectorStart3.zCoord, true));
                mc.thePlayer.motionY = 0.0D;
                break;
            case "librecraft2":
                vectorStart4 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                yaw4 = -mc.thePlayer.rotationYaw;
                pitch4 = -mc.thePlayer.rotationPitch;
                vectorEnd4 = new Vec3(Math.sin(Math.toRadians(yaw4)) * Math.cos(Math.toRadians(pitch4)) * 8.9D + vectorStart4.xCoord, Math.sin(Math.toRadians(pitch4)) * 8.9D + vectorStart4.yCoord, Math.cos(Math.toRadians(yaw4)) * Math.cos(Math.toRadians(pitch4)) * 8.9D + vectorStart4.zCoord);
                mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorEnd4.xCoord, mc.thePlayer.posY + 0.3D, vectorEnd4.zCoord, true));
                mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorStart4.xCoord, mc.thePlayer.posY + 0.3D, vectorStart4.zCoord, true));
                mc.thePlayer.motionY = 0.0D;
                break;
            case "librecraft3":
                vectorStart5 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                yaw5 = -mc.thePlayer.rotationYaw;
                pitch5 = -mc.thePlayer.rotationPitch;
                vectorEnd5 = new Vec3(Math.sin(Math.toRadians(yaw5)) * Math.cos(Math.toRadians(pitch5)) * 5.1D + vectorStart5.xCoord, Math.sin(Math.toRadians(pitch5)) * 5.1D + vectorStart5.yCoord, Math.cos(Math.toRadians(yaw5)) * Math.cos(Math.toRadians(pitch5)) * 5.1D + vectorStart5.zCoord);
                mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorEnd5.xCoord, mc.thePlayer.posY + 0.2D, vectorEnd5.zCoord, true));
                mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorStart5.xCoord, mc.thePlayer.posY + 0.2D, vectorStart5.zCoord, true));
                mc.thePlayer.motionY = 0.0D;
                mc.thePlayer.onGround = true;
                mc.thePlayer.isAirBorne = true;
                break;
            case "librecraft":
                vectorStart2 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                yaw2 = -mc.thePlayer.rotationYaw;
                pitch2 = -mc.thePlayer.rotationPitch;
                vectorEnd2 = new Vec3(Math.sin(Math.toRadians(yaw2)) * Math.cos(Math.toRadians(pitch2)) * 13.9D + vectorStart2.xCoord, Math.sin(Math.toRadians(pitch2)) * 13.9D + vectorStart2.yCoord, Math.cos(Math.toRadians(yaw2)) * Math.cos(Math.toRadians(pitch2)) * 13.9D + vectorStart2.zCoord);
                mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorEnd2.xCoord, mc.thePlayer.posY + 1.0D, vectorEnd2.zCoord, true));
                mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(vectorStart2.xCoord, mc.thePlayer.posY + 1.0D, vectorStart2.zCoord, true));
                mc.thePlayer.motionY = 0.0D;
                break;
            case "flag":
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX * 999, mc.thePlayer.posY + (mc.gameSettings.keyBindJump.isKeyDown() ? 1.5624 : 0.00000001) - (mc.gameSettings.keyBindSneak.isKeyDown() ? 0.0624 : 0.00000002), mc.thePlayer.posZ + mc.thePlayer.motionZ * 999, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX * 999, mc.thePlayer.posY - 6969, mc.thePlayer.posZ + mc.thePlayer.motionZ * 999, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                mc.thePlayer.setPosition(mc.thePlayer.posX + mc.thePlayer.motionX * 11, mc.thePlayer.posY, mc.thePlayer.posZ + mc.thePlayer.motionZ * 11);
                mc.thePlayer.motionY = 0F;
                break;
            case "keepalive":
                mc.getNetHandler().addToSendQueue(new C00PacketKeepAlive());

                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                if(mc.gameSettings.keyBindJump.isKeyDown())
                    mc.thePlayer.motionY += vanillaSpeed;
                if(mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY -= vanillaSpeed;
                MovementUtils.strafe(vanillaSpeed);
                break;
            case "minesecure":
                mc.thePlayer.capabilities.isFlying = false;

                if(!mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY = -0.01F;

                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                MovementUtils.strafe(vanillaSpeed);

                if(mineSecureVClipTimer.hasTimePassed(150) && mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 5, mc.thePlayer.posZ, false));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.5D, -1000, 0.5D, false));
                    final double d1 = Math.toRadians(mc.thePlayer.rotationYaw);
                    final double x = -Math.sin(d1) * 0.4D;
                    final double z = Math.cos(d1) * 0.4D;
                    mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);

                    mineSecureVClipTimer.reset();
                }
                break;
            case "hac":
                mc.thePlayer.motionX *= 0.8;
                mc.thePlayer.motionZ *= 0.8;
            case "hawkeye":
                mc.thePlayer.motionY = mc.thePlayer.motionY <= -0.42 ? 0.42 : -0.42;
                break;
            case "teleportrewinside":
                final Vec3 vectorStart7 = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                final float yaw7= -mc.thePlayer.rotationYaw;
                final float pitch7 = -mc.thePlayer.rotationPitch;
                final double length7 = 9.9;
                final Vec3 vectorEnd7 = new Vec3(
                        Math.sin(Math.toRadians(yaw7)) * Math.cos(Math.toRadians(pitch7)) * length7 + vectorStart7.xCoord,
                        Math.sin(Math.toRadians(pitch7)) * length7 + vectorStart7.yCoord,
                        Math.cos(Math.toRadians(yaw7)) * Math.cos(Math.toRadians(pitch7)) * length7 + vectorStart7.zCoord
                );
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vectorEnd7.xCoord, mc.thePlayer.posY + 2, vectorEnd7.zCoord, true));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vectorStart7.xCoord, mc.thePlayer.posY + 2, vectorStart7.zCoord, true));
                mc.thePlayer.motionY = 0;
                break;
            case "minesucht":
                final double posX = mc.thePlayer.posX;
                final double posY = mc.thePlayer.posY;
                final double posZ = mc.thePlayer.posZ;

                if(!mc.gameSettings.keyBindForward.isKeyDown())
                    break;

                if(System.currentTimeMillis() - minesuchtTP > 99) {
                    final Vec3 vec3 = mc.thePlayer.getPositionEyes(0);
                    final Vec3 vec31 = mc.thePlayer.getLook(0);
                    final Vec3 vec32 = vec3.addVector(vec31.xCoord * 7, vec31.yCoord * 7, vec31.zCoord * 7);

                    if(mc.thePlayer.fallDistance > 0.8) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 50, posZ, false));
                        mc.thePlayer.fall(100, 100);
                        mc.thePlayer.fallDistance = 0;
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 20, posZ, true));
                    }

                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec32.xCoord, mc.thePlayer.posY + 50, vec32.zCoord, true));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec32.xCoord, posY, vec32.zCoord, true));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                    minesuchtTP = System.currentTimeMillis();
                }else{
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, true));
                }
                break;
            case "jetpack":
                if(mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.effectRenderer.spawnEffectParticle(EnumParticleTypes.FLAME.getParticleID(), mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, -mc.thePlayer.motionX, -0.5D, -mc.thePlayer.motionZ);
                    mc.thePlayer.motionY += 0.15D;
                    mc.thePlayer.motionX *= 1.1D;
                    mc.thePlayer.motionZ *= 1.1D;
                }
                break;
            case "mineplex":
                if(mc.thePlayer.inventory.getCurrentItem() == null) {
                    if(mc.gameSettings.keyBindJump.isKeyDown() && mineplexTimer.hasTimePassed(100)) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.6, mc.thePlayer.posZ);
                        mineplexTimer.reset();
                    }

                    if(mc.thePlayer.isSneaking() && mineplexTimer.hasTimePassed(100)) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.6, mc.thePlayer.posZ);
                        mineplexTimer.reset();
                    }

                    final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1, mc.thePlayer.posZ);
                    final Vec3 vec = new Vec3(blockPos).addVector(0.4F, 0.4F, 0.4F).add(new Vec3(EnumFacing.UP.getDirectionVec()));
                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockPos, EnumFacing.UP, new Vec3(vec.xCoord * 0.4F, vec.yCoord * 0.4F, vec.zCoord * 0.4F));
                    MovementUtils.strafe(0.27F);

                    mc.timer.timerSpeed = (1 + mineplexSpeedValue.get());
                }else{
                    mc.timer.timerSpeed = 1;
                    setState(false);
                    ClientUtils.displayChatMessage("§8[§c§lMineplex-§a§lFly§8] §aSelect an empty slot to fly.");
                }
                break;
            case "aac3.3.12":
                if(mc.thePlayer.posY < -70)
                    mc.thePlayer.motionY = aacMotion.get();

                mc.timer.timerSpeed = 1F;

                if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    mc.timer.timerSpeed = 0.2F;
                    mc.rightClickDelayTimer = 0;
                }
                break;
            case "aac3.3.12-glide":
                if(!mc.thePlayer.onGround)
                    aac3glideDelay++;

                if(aac3glideDelay == 2)
                    mc.timer.timerSpeed = 1F;

                if(aac3glideDelay == 12)
                    mc.timer.timerSpeed = 0.1F;

                if(aac3glideDelay >= 12 && !mc.thePlayer.onGround) {
                    aac3glideDelay = 0;
                    mc.thePlayer.motionY = .015;
                }
                break;
            case "aac3.3.13":
                if(mc.thePlayer.isDead)
                    wasDead = true;

                if(wasDead || mc.thePlayer.onGround) {
                    wasDead = false;

                    mc.thePlayer.motionY = aacMotion2.get();
                    mc.thePlayer.onGround = false;
                }

                mc.timer.timerSpeed = 1F;

                if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    mc.timer.timerSpeed = 0.2F;
                    mc.rightClickDelayTimer = 0;
                }
                break;
            case "watchcat":
                MovementUtils.strafe(0.15F);
                mc.thePlayer.setSprinting(true);

                if(mc.thePlayer.posY < startY + 2) {
                    mc.thePlayer.motionY = Math.random() * 0.5;
                    break;
                }

                if(startY > mc.thePlayer.posY)
                    MovementUtils.strafe(0F);
                break;
            case "spartan":
                mc.thePlayer.motionY = 0;
                spartanTimer.update();
                if(spartanTimer.hasTimePassed(12)) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 8, mc.thePlayer.posZ, true));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 8, mc.thePlayer.posZ, true));
                    spartanTimer.reset();
                }
                break;
            case "spartan2":
                MovementUtils.strafe(0.264F);

                if(mc.thePlayer.ticksExisted % 8 == 0)
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 10, mc.thePlayer.posZ, true));
                break;
            case "neruxvace":
                if (!mc.thePlayer.onGround)
                    aac3glideDelay++;

                if (aac3glideDelay >= neruxVaceTicks.get() && !mc.thePlayer.onGround) {
                    aac3glideDelay = 0;
                    mc.thePlayer.motionY = .015;
                }
                break;
            case "hypixel":
                final int boostDelay = hypixelBoostDelay.get();
                if (hypixelBoost.get() && !flyTimer.hasTimePassed(boostDelay)) {
                    mc.timer.timerSpeed = 1F + (hypixelBoostTimer.get() * ((float) flyTimer.hasTimeLeft(boostDelay) / (float) boostDelay));
                }
                hypixelTimer.update();
                if (hypixelTimer.hasTimePassed(2)) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-5, mc.thePlayer.posZ);
                    hypixelTimer.reset();
                }
                break;
            case "freehypixel":
                if(freeHypixelTimer.hasTimePassed(10)) {
                    mc.thePlayer.capabilities.isFlying = true;
                    break;
                }else{
                    mc.thePlayer.rotationYaw = freeHypixelYaw;
                    mc.thePlayer.rotationPitch = freeHypixelPitch;
                    mc.thePlayer.motionX = mc.thePlayer.motionZ = mc.thePlayer.motionY = 0;
                }

                if(startY == new BigDecimal(mc.thePlayer.posY).setScale(3, RoundingMode.HALF_DOWN).doubleValue())
                    freeHypixelTimer.update();
                break;
            case "bugspartan":
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.thePlayer.motionY += vanillaSpeed;
                if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY -= vanillaSpeed;
                MovementUtils.strafe(vanillaSpeed);
                break;
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (this.modeValue.get().equalsIgnoreCase("boosthypixel")) {
            double xDist;
            double zDist;
            switch (event.getEventState()) {
                case POST:
                    xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                    zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                    this.lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
                    break;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.modeValue.get().equalsIgnoreCase("boosthypixel")) {
            if (!this.failedStart)
                mc.thePlayer.motionY = 0.0D;

            counter++;
            switch (counter) {
                case 0: {
                    //e.setY(mc.player.posY);
                    break;
                }
                case 3: {
                    y = -y;
                    break;
                }
                case 7: {
                    y = -y;
                    break;
                }
                case 12: {
                    y = -y;
                    counter = 0;
                    break;
                }
                default: {
                    y += 0.0001;
                    break;
                }
            }
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ);
        }
    }

    public static double round(double var0, int var2) {
        if(var2 < 0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal var3 = new BigDecimal(var0);
            var3 = var3.setScale(var2, RoundingMode.HALF_UP);
            return var3.doubleValue();
        }
    }
    public boolean isOnGround(double var1) {
        return (!this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0D, -var1, 0.0D)).isEmpty()?true:false);
    }
    public static Block getBlockUnderPlayer(EntityPlayer var0, double var1) {
        return mc.theWorld.getBlockState(new BlockPos(var0.posX, var0.posY - var1, var0.posZ)).getBlock();
    }
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        final String mode = modeValue.get();

        if (!markValue.get() || mode.equalsIgnoreCase("Vanilla") || mode.equalsIgnoreCase("SmoothVanilla"))
            return;

        double y = startY + 2D;

        if(headValue.get()) {
            RenderUtils.drawPlatform(y, mc.thePlayer.getEntityBoundingBox().maxY < y ? new Color(0, 255, 0, 90) : new Color(255, 0, 0, 90), 1);
        }

        switch (mode.toLowerCase()) {
            case "aac1.9.10":
                RenderUtils.drawPlatform(startY + aacJump, new Color(45, 255, 238, 90), 1);
                break;
            case "aac3.3.12":
                RenderUtils.drawPlatform(-70, new Color(53, 255, 220, 90), 1);
                break;
        }
    }
    @EventTarget
    public void onPacket(PacketEvent event) {
        if(noPacketModify)
            return;

        final Packet<?> packet = event.getPacket();

        if(packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;

            final String mode = modeValue.get();

            if (mode.equalsIgnoreCase("NCP") || mode.equalsIgnoreCase("Rewinside") ||
                    (mode.equalsIgnoreCase("Mineplex") && mc.thePlayer.inventory.getCurrentItem() == null))
                packetPlayer.onGround = true;
        }

        if(packet instanceof S08PacketPlayerPosLook) {
            if(lagback.get()) {
                ClientUtils.displayChatMessage("§8[§c§lLagBackCheck§8] §cSetback detected.");
                this.toggle();
            }
        }
    }
    public static float getMaxFallDist() {
        PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        return mc.thePlayer.getMaxFallHeight() + f;
    }
    @EventTarget
    public void onMove(final MoveEvent event) {
        double amplifier;
        double baseSpeed;
        double d1;
        switch(modeValue.get().toLowerCase()) {
            case "cubecraft2":
                double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                if (this.cubecraftTeleportTickTimer2.hasTimePassed(2)) {
                    mc.thePlayer.motionY = 0.0D;
                    mc.thePlayer.onGround = true;
                    event.setX(-Math.sin(yaw) * 2.0D);
                    event.setZ(Math.cos(yaw) * 2.0D);
                    this.cubecraftTeleportTickTimer2.reset();
                    break;
                }
                event.setX(-Math.sin(yaw) * 0.1D);
                event.setZ(Math.cos(yaw) * 0.1D);
                break;
            case "infinitecubecraft":
                yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                if (this.cubecraftTeleportTickTimer3.hasTimePassed(2)) {
                    event.setX(-Math.sin(yaw) * 2.0D);
                    event.setZ(Math.cos(yaw) * 2.0D);
                    this.cubecraftTeleportTickTimer3.reset();
                    break;
                }
                event.setX(-Math.sin(yaw) * 0.1D);
                event.setZ(Math.cos(yaw) * 0.1D);
                break;
            case "cubecraft": {
                final double yaw2 = Math.toRadians(mc.thePlayer.rotationYaw);

                if (cubecraftTeleportTickTimer.hasTimePassed(2)) {
                    event.setX(-Math.sin(yaw2) * 2.4D);
                    event.setZ(Math.cos(yaw2) * 2.4D);

                    cubecraftTeleportTickTimer.reset();
                } else {
                    event.setX(-Math.sin(yaw2) * 0.2D);
                    event.setZ(Math.cos(yaw2) * 0.2D);
                }
                break;
            }
            case "glidevanilla":
                mc.thePlayer.motionY = -0.5D;
                break;
            case "boosthypixel":
                if (!MovementUtils.isMoving()) {
                    event.setX(0.0D);
                    event.setZ(0.0D);
                    break;
                }
                if (this.failedStart)
                    break;
                amplifier = 1.0D + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (0.2D * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)) : 0.0D);
                baseSpeed = 0.29D * amplifier;
                switch (this.boostHypixelState) {
                    case 1:
                        this.moveSpeed = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56D : 2.034D) * baseSpeed;
                        this.boostHypixelState = 2;
                        break;
                    case 2:
                        this.moveSpeed *= 2.16D;
                        this.boostHypixelState = 3;
                        break;
                    case 3:
                        this.moveSpeed = this.lastDistance - ((mc.thePlayer.ticksExisted % 2 == 0) ? 0.0103D : 0.0123D) * (this.lastDistance - baseSpeed);
                        this.boostHypixelState = 4;
                        break;
                    default:
                        this.moveSpeed = this.lastDistance - this.lastDistance / 159.8D;
                        break;
                }
                this.moveSpeed = Math.max(this.moveSpeed, 0.3D);
                d1 = MovementUtils.getDirection();
                event.setX(-Math.sin(d1) * this.moveSpeed);
                event.setZ(Math.cos(d1) * this.moveSpeed);
                mc.thePlayer.motionX = event.getX();
                mc.thePlayer.motionZ = event.getZ();
                break;
            case "freehypixel":
                if (!freeHypixelTimer.hasTimePassed(10))
                    event.zero();
                break;
        }
    }

    @EventTarget
    public void onBB(final BlockBBEvent event) {
        if (mc.thePlayer == null) return;

        final String mode = modeValue.get();

        if (event.getBlock() instanceof BlockAir && (mode.equalsIgnoreCase("Hypixel") ||
                mode.equalsIgnoreCase("BoostHypixel") || mode.equalsIgnoreCase("Rewinside") ||
                (mode.equalsIgnoreCase("Mineplex") && mc.thePlayer.inventory.getCurrentItem() == null)) && event.getY() < mc.thePlayer.posY)
            event.setBoundingBox(AxisAlignedBB.fromBounds(event.getX(), event.getY(), event.getZ(), event.getX() + 1, mc.thePlayer.posY, event.getZ() + 1));
    }

    @EventTarget
    public void onJump(final JumpEvent e) {
        final String mode = modeValue.get();

        if (mode.equalsIgnoreCase("Hypixel") || mode.equalsIgnoreCase("BoostHypixel") ||
                mode.equalsIgnoreCase("Rewinside") || (mode.equalsIgnoreCase("Mineplex") && mc.thePlayer.inventory.getCurrentItem() == null))
            e.cancelEvent();
    }

    @EventTarget
    public void onStep(final StepEvent e) {
        final String mode = modeValue.get();

        if (mode.equalsIgnoreCase("Hypixel") || mode.equalsIgnoreCase("BoostHypixel") ||
                mode.equalsIgnoreCase("Rewinside") || (mode.equalsIgnoreCase("Mineplex") && mc.thePlayer.inventory.getCurrentItem() == null))
            e.setStepHeight(0F);
    }

    private void handleVanillaKickBypass() {
        if(!vanillaKickBypassValue.get() || !groundTimer.hasTimePassed(1000)) return;

        final double ground = calculateGround();

        for(double posY = mc.thePlayer.posY; posY > ground; posY -= 8D) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true));

            if(posY - 8D < ground) break; // Prevent next step
        }

        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, ground, mc.thePlayer.posZ, true));


        for(double posY = ground; posY < mc.thePlayer.posY; posY += 8D) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true));

            if(posY + 8D > mc.thePlayer.posY) break; // Prevent next step
        }

        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));

        groundTimer.reset();
    }

    // TODO: Make better and faster calculation lol
    private double calculateGround() {
        final AxisAlignedBB playerBoundingBox = mc.thePlayer.getEntityBoundingBox();
        double blockHeight = 1D;

        for(double ground = mc.thePlayer.posY; ground > 0D; ground -= blockHeight) {
            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);

            if(mc.theWorld.checkBlockCollision(customBox)) {
                if(blockHeight <= 0.05D)
                    return ground + blockHeight;

                ground += blockHeight;
                blockHeight = 0.05D;
            }
        }

        return 0F;
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}