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

@ModuleInfo(name = "AutoTeam", description = "Auto Team.", category = ModuleCategory.MISC)
public class AutoTeam extends Module {
    int Teams = 0;
    private final ListValue ModeValue = new ListValue("Mode", new String[]{"HuaYuTing"}, "HuaYuTing");
    private final BoolValue ADValue = new BoolValue("AD", true);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            switch (ModeValue.get().toLowerCase()) {
                case "huayuting":
                    if (message.contains(" 被消灭!")) {
                        String Team = message.replaceAll(" 被消灭!", "");
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? "[HDebug]" : "") + "检测到" + Team + "被消灭" + (ADValue.get() ? " HDebugQQ群:1128533970" : ""));
                        Teams = Teams + 1;
                    }
                    break;
            }
        }
    }
    @Override
    public String getTag() {
        return ModeValue.get() + " Teams:" + Teams;
    }
}