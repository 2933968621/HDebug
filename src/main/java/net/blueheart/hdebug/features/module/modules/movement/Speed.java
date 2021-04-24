package net.blueheart.hdebug.features.module.modules.movement;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.*;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.features.module.modules.movement.speeds.SpeedMode;
import net.blueheart.hdebug.features.module.modules.movement.speeds.aac.*;
import net.blueheart.hdebug.features.module.modules.movement.speeds.ncp.*;
import net.blueheart.hdebug.features.module.modules.movement.speeds.other.*;
import net.blueheart.hdebug.features.module.modules.movement.speeds.spartan.SpartanYPort;
import net.blueheart.hdebug.features.module.modules.movement.speeds.spectre.SpectreBHop;
import net.blueheart.hdebug.features.module.modules.movement.speeds.spectre.SpectreLowHop;
import net.blueheart.hdebug.features.module.modules.movement.speeds.spectre.SpectreOnGround;
import net.blueheart.hdebug.utils.ClientUtils;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.AAC.*;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Hypixel.ChinaHypixel;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Hypixel.HypixelHop;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Hypixel.USHypixel;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Matrix.CustomMatrixHop;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Matrix.CustomMatrixHop2;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Matrix.MatrixHop;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.NCP.NCPBHop2;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Speed.Other.BHop;
import org.me.ByBlueHeart.HDebugClient.Modules.World.BlockFly;
import org.me.ByBlueHeart.HDebugClient.Modules.World.HypixelBlockFly;

import java.util.ArrayList;
import java.util.List;

;

@ModuleInfo(name = "Speed", description = "Allows you to move faster.", category = ModuleCategory.MOVEMENT)
public class Speed extends Module {

    private final SpeedMode[] speedModes = new SpeedMode[] {
            // NCP
            new NCPBHop(),
            new NCPBHop2(),
            new NCPFHop(),
            new SNCPBHop(),
            new NCPHop(),
            new YPort(),
            new YPort2(),
            new NCPYPort(),
            new Boost(),
            new Frame(),
            new MiJump(),
            new OnGround(),

            // AAC
            new AACBHop(),
            new AAC2BHop(),
            new AAC3BHop(),
            new AAC3310BHop(),
            new AAC4BHop(),
            new AAC5BHop(),
            new AAC6BHop(),
            new AAC7BHop(),
            new AACHop3313(),
            new AACHop350(),
            new AACLowHop(),
            new AACLowHop2(),
            new AACLowHop3(),
            new AACLowHop4(),
            new AACLowHop5(),
            new AAC4(),
            new AACSlowHop(),
            new AAC4312Hop(),
            new AAC4FastHop(),
            new AACGround(),
            new AACGround2(),
            new AACYPort(),
            new AACYPort2(),
            new AACPort(),
            new AACv4CustomHop(),
            new AACv4CustomHop2(),
            new OldAACBHop(),

            // Spartan
            new SpartanYPort(),

            // Spectre
            new SpectreLowHop(),
            new SpectreBHop(),
            new SpectreOnGround(),
            new TeleportCubeCraft(),

            // Server
            new HiveHop(),
            new USHypixel(),
            new MineplexGround(),
            new HypixelHop(),
            new ChinaHypixel(),
            new CustomMatrixHop(),
            new CustomMatrixHop2(),
            new MatrixHop(),

            // Other
            new SlowHop(),
            new CustomSpeed(),
            new BHop()
    };

    public final ListValue modeValue = new ListValue("Mode", getModes(), "NCPBHop") {

        @Override
        protected void onChange(final String oldValue, final String newValue) {
            if(getState())
                onDisable();
        }

        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            if(getState())
                onEnable();
        }
    };

    public final FloatValue customSpeedValue = new FloatValue("CustomSpeed", 1.6F, 0.2F, 2F);
    public final FloatValue customYValue = new FloatValue("CustomY", 0F, 0F, 4F);
    public final FloatValue customTimerValue = new FloatValue("CustomTimer", 1F, 0.1F, 2F);
    public final BoolValue customStrafeValue = new BoolValue("CustomStrafe", true);
    public final BoolValue resetXZValue = new BoolValue("CustomResetXZ", false);
    public final BoolValue resetYValue = new BoolValue("CustomResetY", false);
    public final FloatValue aac4customHopTimerValue = new FloatValue("AACv4CustomHop-Timer1", 1.0F, 1.0F, 3.4F);
    public final FloatValue aac4customHopTimerValue2 = new FloatValue("AACv4CustomHop-Timer2", 0.4F, 0.2F, 1.0F);
    public final FloatValue aac4customHop2TimerValue = new FloatValue("AACv4CustomHop2-Timer2", 1.0F, 1.0F, 3.4F);
    public final FloatValue aac4customHop2TimerValue2 = new FloatValue("AACv4CustomHop2-Timer1", 0.4F, 0.2F, 1.0F);
    public final FloatValue custommatrixHopTimerValue = new FloatValue("CustomMatrixHop-Timer1", 1.0F, 1.0F, 2.9F);
    public final FloatValue custommatrixHopTimerValue2 = new FloatValue("CustomMatrixHop-Timer2", 0.6F, 0.3F, 1.1F);
    public final FloatValue custommatrixHop2TimerValue = new FloatValue("CustomMatrixHop2-Timer2", 1.0F, 1.0F, 2.9F);
    public final FloatValue custommatrixHop2TimerValue2 = new FloatValue("CustomMatrixHop2-Timer1", 0.6F, 0.3F, 1.1F);
    public final BoolValue lagback = new BoolValue("LagBackCheck", true);
    public final FloatValue portMax = new FloatValue("AAC-PortLength", 1, 1, 20);
    public final FloatValue aacGroundTimerValue = new FloatValue("AACGround-Timer", 3F, 1.1F, 10F);
    public final FloatValue cubecraftPortLengthValue = new FloatValue("CubeCraft-PortLength", 1F, 0.1F, 2F);
    public final FloatValue mineplexGroundSpeedValue = new FloatValue("MineplexGround-Speed", 0.5F, 0.1F, 1F);

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if(mc.thePlayer.isSneaking())
            return;

        if(MovementUtils.isMoving())
            mc.thePlayer.setSprinting(true);

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onUpdate();
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
        if(mc.thePlayer.isSneaking() || event.getEventState() != EventState.PRE)
            return;

        if(MovementUtils.isMoving())
            mc.thePlayer.setSprinting(true);

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onMotion();
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        HypixelBlockFly i5 = (HypixelBlockFly) HDebug.moduleManager.getModule(HypixelBlockFly.class);
        BlockFly i4 = (BlockFly) HDebug.moduleManager.getModule(BlockFly.class);
        if(i4.getState() == true && BlockFly.LagValue.get() == true || i5.getState() == true && HypixelBlockFly.LagValue.get() == true)
            return;

        if(mc.thePlayer.isSneaking())
            return;

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onMove(event);
    }

    @EventTarget
    public void onTick(final TickEvent event) {
        if(mc.thePlayer.isSneaking())
            return;

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onTick();
    }
    @EventTarget
    public void onPacket(final PacketEvent event){
        final Packet<?> packet = event.getPacket();
        if(packet instanceof S08PacketPlayerPosLook){
            if(lagback.get()){
                ClientUtils.displayChatMessage("§8[§c§lLagBackCheck§8] §cSetback detected.");
                this.toggle();
            }
        }
    }

    @Override
    public void onEnable() {
        if(mc.thePlayer == null)
            return;

        mc.timer.timerSpeed = 1F;

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onEnable();
    }

    @Override
    public void onDisable() {
        if(mc.thePlayer == null)
            return;

        mc.timer.timerSpeed = 1F;

        final SpeedMode speedMode = getMode();

        if(speedMode != null)
            speedMode.onDisable();
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }

    private SpeedMode getMode() {
        final String mode = modeValue.get();

        for(final SpeedMode speedMode : speedModes)
            if(speedMode.modeName.equalsIgnoreCase(mode))
                return speedMode;

        return null;
    }

    private String[] getModes() {
        final List<String> list = new ArrayList<>();
        for(final SpeedMode speedMode : speedModes)
            list.add(speedMode.modeName);
        return list.toArray(new String[0]);
    }
}
