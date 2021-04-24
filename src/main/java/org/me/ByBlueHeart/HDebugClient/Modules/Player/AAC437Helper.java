package org.me.ByBlueHeart.HDebugClient.Modules.Player;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.event.Render3DEvent;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.features.module.modules.render.Breadcrumbs;
import net.blueheart.hdebug.utils.ClientUtils;
import net.blueheart.hdebug.utils.render.ColorUtils;
import net.blueheart.hdebug.utils.render.RenderUtils;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "AAC4.3.7Helper", description = "Cooperate with Speed Flight LongJump Phase.", category = ModuleCategory.PLAYER)
public class AAC437Helper extends Module {

    private final List<Packet> packets = new ArrayList<>();
    private EntityOtherPlayerMP fakePlayer = null;
    private boolean disableLogger;
    private final LinkedList<double[]> positions = new LinkedList<>();

    private final MSTimer pulseTimer = new MSTimer();

    @Override
    public void onEnable() {
        if(mc.thePlayer == null)
            return;

        fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        fakePlayer.clonePlayer(mc.thePlayer, true);
        fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
        fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
        mc.theWorld.addEntityToWorld(-9100, fakePlayer);

        synchronized(positions) {
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }

        pulseTimer.reset();
    }

    @Override
    public void onDisable() {
        if(mc.thePlayer == null || fakePlayer == null)
            return;

        HuaYuTingHelper();
        mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
        fakePlayer = null;
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
    public void onUpdate(UpdateEvent event) {
        synchronized(positions) {
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }

        if(true && pulseTimer.hasTimePassed(500)) {
            HuaYuTingHelper();
            pulseTimer.reset();
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        final Breadcrumbs breadcrumbs = (Breadcrumbs) HDebug.moduleManager.getModule(Breadcrumbs.class);
        final Color color = breadcrumbs.colorRainbow.get() ? ColorUtils.rainbow() : new Color(breadcrumbs.colorRedValue.get(), breadcrumbs.colorGreenValue.get(), breadcrumbs.colorBlueValue.get());

        synchronized(positions) {
            glPushMatrix();

            glDisable(GL_TEXTURE_2D);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_LINE_SMOOTH);
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            mc.entityRenderer.disableLightmap();
            glBegin(GL_LINE_STRIP);
            RenderUtils.glColor(color);
            final double renderPosX = mc.getRenderManager().viewerPosX;
            final double renderPosY = mc.getRenderManager().viewerPosY;
            final double renderPosZ = mc.getRenderManager().viewerPosZ;

            for(final double[] pos : positions)
                glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ);

            glColor4d(1, 1, 1, 1);
            glEnd();
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_LINE_SMOOTH);
            glDisable(GL_BLEND);
            glEnable(GL_TEXTURE_2D);
            glPopMatrix();
        }
    }

    @Override
    public String getTag() {
        return String.valueOf("Speed&Flight&LongJump&Phase Mode");
    }

    private void HuaYuTingHelper() {
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
