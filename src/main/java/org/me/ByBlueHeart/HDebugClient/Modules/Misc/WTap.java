package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@ModuleInfo(name = "WTap", description = "Auto W Tap.", category = ModuleCategory.MISC)
public class WTap extends Module {
    @EventTarget
    private void onTick(PacketEvent e) {
        C02PacketUseEntity packet;
        if (e.getPacket() instanceof C02PacketUseEntity && mc.thePlayer != null && (packet = (C02PacketUseEntity)e.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && packet.getEntityFromWorld(mc.theWorld) != mc.thePlayer && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
            boolean sprint = mc.thePlayer.isSprinting();
            mc.thePlayer.setSprinting(false);
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            mc.thePlayer.setSprinting(sprint);
        }
    }
}