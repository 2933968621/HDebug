package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(name = "JoinSay", description = "Player Join Game Auto Say.", category = ModuleCategory.MISC)
public class JoinSay extends Module {
    int Number = 0;
    private final ListValue ModeValue = new ListValue("Mode", new String[]{"ChinaHypixel", "HuaYuTing"}, "HuaYuTing");
    private final BoolValue ADValue = new BoolValue("AD", true);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);
    private final BoolValue PlayerNameValue = new BoolValue("PlayerName", true);

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String AD = " HDebugQQ群:1128533970";
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            switch (ModeValue.get().toLowerCase()) {
                case "chinahypixel":
                    if (message.contains(" 加入了游戏!")) {
                        String username = message.replaceAll(" 加入了游戏!", "");
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? "[HDebug]" : "") + (PlayerNameValue.get() ? username : "") + " Hello My Friends Welcome You Join" + (ADValue.get() ? AD : ""));
                        Number = Number + 1;
                    }
                    break;
                case "huayuting":
                    if (message.contains(" 加入了游戏!")) {
                        String username = message.replaceAll(" 加入了游戏!", "");
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? "[HDebug]" : "") + (PlayerNameValue.get() ? username : "") + " Hello My Friends Welcome You Join" + (ADValue.get() ? AD : ""));
                        Number = Number + 1;
                    }
                    break;
            }
        }
    }
    @Override
    public String getTag() {
        return ModeValue.get() + " All Player Number:" + Number;
    }
}