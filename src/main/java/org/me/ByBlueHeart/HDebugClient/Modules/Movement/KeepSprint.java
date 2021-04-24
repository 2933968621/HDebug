package org.me.ByBlueHeart.HDebugClient.Modules.Movement;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@ModuleInfo(name = "KeepSprint", description = "Automatically sprints all the time.", category = ModuleCategory.MOVEMENT)
public class KeepSprint extends Module {

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();
            if (packet.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                event.cancelEvent();
            }
        }
    }

}