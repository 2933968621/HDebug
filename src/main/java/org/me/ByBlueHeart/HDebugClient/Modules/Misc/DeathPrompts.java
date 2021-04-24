package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.Logger;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.realms.RealmsSharedConstants;

@ModuleInfo(name = "DeathPrompts", description = "Death Prompts.", category = ModuleCategory.MISC)
public class DeathPrompts extends Module {
    int Died = 0;

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            if (message.contains("您获得了5秒的无敌时间!")) {
                Logger.printinfo("警告 你死亡了一次!");
                Died = Died + 1;
            }
        }
    }
    @Override
    public String getTag() {
        return " Died Number:" + Died;
    }
}