package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(name = "AutoReJoin", description = "Auto Re Join Your Game.", category = ModuleCategory.MISC)
public class AutoReJoin extends Module {
    int Kick = 0;
    private final ListValue ModeValue = new ListValue("Mode", new String[]{"ChinaHypixel"}, "ChinaHypixel");

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            switch (ModeValue.get().toLowerCase()) {
                case "chinahypixel":
                    if (message.contains("你被踢出了游戏")) {
                        mc.thePlayer.sendChatMessage("/rejoin");
                        Kick = Kick + 1;
                    }
                    if (message.contains("你的网络连接出现小问题")) {
                        mc.thePlayer.sendChatMessage("/rejoin");
                        Kick = Kick + 1;
                    }
                    break;
            }
        }
    }
    @Override
    public String getTag() {
        return ModeValue.get() + " Kick Number:" + Kick;
    }
}