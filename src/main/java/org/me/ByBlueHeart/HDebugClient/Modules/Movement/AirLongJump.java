package org.me.ByBlueHeart.HDebugClient.Modules.Movement;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.MotionEvent;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@ModuleInfo(name = "AirLongJump", description = "Use LongJump in Air.", category = ModuleCategory.MOVEMENT)
public class AirLongJump extends Module {

    private final List<Packet> packets = new ArrayList<>();
    private final LinkedList<double[]> positions = new LinkedList<>();
    private boolean disableLogger;

    @Override
    public void onEnable() {

        synchronized(positions) {
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }


    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        if(mc.thePlayer == null)
            return;

        blink();

    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if (mc.thePlayer == null || disableLogger)
            return;

        if (packet instanceof C03PacketPlayer) // Cancel all movement stuff
            event.cancelEvent();

        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook ||
                packet instanceof C08PacketPlayerBlockPlacement ||
                packet instanceof C0APacketAnimation ||
                packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
            event.cancelEvent();

            packets.add(packet);
        }
    }

    @EventTarget
    public void onMotion(MotionEvent e){

        if(MovementUtils.isMoving()) {
            mc.timer.timerSpeed = 1.0F;

            if(mc.thePlayer.onGround) {
                MovementUtils.strafe(8.0F);
                mc.thePlayer.motionY = 0.42F;
            }
            MovementUtils.strafe(8.0F);
        } else {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0D;
        }

    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        synchronized(positions) {
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }
    }

    private void blink() {
        try {
            disableLogger = true;

            final Iterator<Packet> packetIterator = packets.iterator();
            for(; packetIterator.hasNext(); ) {
                mc.getNetHandler().addToSendQueue(packetIterator.next());
                packetIterator.remove();
            }

            disableLogger = false;
        }catch(final Exception e) {
            e.printStackTrace();
            disableLogger = false;
        }

        synchronized(positions) {
            positions.clear();
        }
    }
}