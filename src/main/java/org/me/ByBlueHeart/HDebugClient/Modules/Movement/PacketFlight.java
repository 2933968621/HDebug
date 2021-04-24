package org.me.ByBlueHeart.HDebugClient.Modules.Movement;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.ClientUtils;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
;

@ModuleInfo(name = "PacketFlight", description = "Just another packet flight.", category = ModuleCategory.MOVEMENT)
public class PacketFlight extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[] { "Fast", "Slow" }, "Slow");

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc.thePlayer.motionY = 0.0D;
        mc.thePlayer.onGround = true;
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (mc.thePlayer == null || mc.thePlayer.isSneaking())
            return;
        switch ((this.modeValue.get()).toLowerCase()) {
            case "fast":
                if (mc.gameSettings.keyBindForward.isKeyDown())
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 5.0D, mc.thePlayer.posY, mc.thePlayer.posZ + 5.0D, false));
                if (mc.gameSettings.keyBindBack.isKeyDown())
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - 5.0D, mc.thePlayer.posY, mc.thePlayer.posZ - 5.0D, false));
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 5.0D, mc.thePlayer.posZ, false));
                if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 5.0D, mc.thePlayer.posZ, false));
            case "slow":
                if (mc.gameSettings.keyBindForward.isKeyDown())
                    mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 0.4D, mc.thePlayer.posY, mc.thePlayer.posZ + 0.4D, false));
                if (mc.gameSettings.keyBindBack.isKeyDown())
                    mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - 0.4D, mc.thePlayer.posY, mc.thePlayer.posZ - 0.4D, false));
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.4D, mc.thePlayer.posZ, false));
                if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.4D, mc.thePlayer.posZ, false));
                break;
        }
    }
    public String getTag() {
        return this.modeValue.get();
    }
}
