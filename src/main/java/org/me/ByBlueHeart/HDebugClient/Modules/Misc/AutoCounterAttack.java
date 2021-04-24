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

import java.util.Random;

@ModuleInfo(name = "AutoCounterAttack", description = "Auto Counter Attack Abuse Your Player.", category = ModuleCategory.MISC)
public class AutoCounterAttack extends Module {
    int CA = 0;
    private final ListValue ModeValue = new ListValue("Mode", new String[]{"HuaYuTing", "ChinaHypixel"}, "HuaYuTing");
    private final BoolValue ADValue = new BoolValue("AD", true);
    private final BoolValue AbuseValue = new BoolValue("Abuse", true);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);
    String[] AbuseText = {"我草泥马的你是不是个fw啊你在这L不L的", "你说说你除了会扣L你还会干什么啊嗯？", "宁也很L呢你知不知道呀", "fw我是你大爷你知道吗 你知道就不要在这里LLL的了好吗"};

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            Random r = new Random();
            String Abuse = AbuseText[r.nextInt(57)];
            String AD = "HDebugQQ群:1128533970";
            String PrefixText = "[HDebug]";
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            switch (ModeValue.get().toLowerCase()) {
                case "huayuting":
                    if (message.contains(mc.thePlayer.getGameProfile().getName() + " L")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains(mc.thePlayer.getGameProfile().getName() + "L")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains("L" + mc.thePlayer.getGameProfile().getName())) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains(" L" + mc.thePlayer.getGameProfile().getName())) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains("L " + mc.thePlayer.getGameProfile().getName())) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains(" L " + mc.thePlayer.getGameProfile().getName())) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    break;
                case "chinahypixel":
                    if (message.contains(mc.thePlayer.getGameProfile().getName() + " L")) {
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains(mc.thePlayer.getGameProfile().getName() + "L")) {
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains("L" + mc.thePlayer.getGameProfile().getName())) {
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains(" L" + mc.thePlayer.getGameProfile().getName())) {
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains("L " + mc.thePlayer.getGameProfile().getName())) {
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    if (message.contains(" L " + mc.thePlayer.getGameProfile().getName())) {
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                        CA = CA + 1;
                    }
                    break;
            }
        }
    }
    @Override
    public String getTag() {
        return ModeValue.get() + " CounterAttack:" + CA;
    }
}