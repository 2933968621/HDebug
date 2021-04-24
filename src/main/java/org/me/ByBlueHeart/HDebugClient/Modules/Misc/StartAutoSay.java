package org.me.ByBlueHeart.HDebugClient.Modules.Misc;


import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.ui.client.hud.element.elements.Notification;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(name = "StartAutoSay", description = "RoundBegan Say Game Start.", category = ModuleCategory.MISC)
public class StartAutoSay extends Module {
    int Start = 0;
    private final ListValue ModeValue = new ListValue("Mode", new String[]{"Hypixel", "HuaYuTing"}, "HuaYuTing");
    private final BoolValue ADValue = new BoolValue("AD", true);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String AD = " HDebugQQ群:1128533970";
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            switch (ModeValue.get().toLowerCase()) {
                case "hypixel":
                    if (message.contains("游戏将在1秒后开始！")) {
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? "[HDebug]" : "") + "游戏开始!" + (ADValue.get() ? AD : ""));
                        HDebug.hud.addNotification(new Notification("Game Start!"));
                        Start = Start + 1;
                    }
                    break;
                case "huayuting":
                    if (message.contains("游戏开始 ...")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? "[HDebug]" : "") + "游戏开始!" + (ADValue.get() ? AD : ""));
                        HDebug.hud.addNotification(new Notification("Game Start!"));
                        Start = Start + 1;
                    }
                    break;
            }
        }
    }
    @Override
    public String getTag() {
        return ModeValue.get() + " Start:" + Start;
    }
}