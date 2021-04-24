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

@ModuleInfo(name = "AutoBed", description = "Break Bed Auto Say in Server.", category = ModuleCategory.MISC)
public class AutoBed extends Module {
    int Break = 0;
    private final ListValue ModeValue = new ListValue("Mode", new String[]{"ChinaHypixel", "HuaYuTing"}, "ChinaHypixel");
    private final BoolValue ADValue = new BoolValue("AD", true);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String PrefixText = "[HDebug]";
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            switch (ModeValue.get().toLowerCase()) {
                case "chinahypixel":
                    if (message.contains("红队 Bed 被破坏，击杀者： " + mc.thePlayer.getGameProfile().getName() + "!")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? PrefixText : "") + "我破坏了红队的床! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("You Break Red Bed!"));
                        Break = Break + 1;
                    }
                    if (message.contains("蓝队 Bed 被破坏，击杀者： " + mc.thePlayer.getGameProfile().getName() + "!")) {
                        HDebug.hud.addNotification(new Notification("You Break Blue Bed!"));
                        Break = Break + 1;
                    }
                    if (message.contains("绿队 Bed 被破坏，击杀者： " + mc.thePlayer.getGameProfile().getName() + "!")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? PrefixText : "") + "我破坏了绿队的床! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("You Break Green Bed!"));
                        Break = Break + 1;
                    }
                    if (message.contains("黄队 Bed 被破坏，击杀者： " + mc.thePlayer.getGameProfile().getName() + "!")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? PrefixText : "") + "我破坏了黄队的床! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("You Break Yellow Bed!"));
                        Break = Break + 1;
                    }
                    if (message.contains("青队 Bed 被破坏，击杀者： " + mc.thePlayer.getGameProfile().getName() + "!")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? PrefixText : "") + "我破坏了青队的床! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("You Break Cyan Bed!"));
                        Break = Break + 1;
                    }
                    if (message.contains("白队 Bed 被破坏，击杀者： " + mc.thePlayer.getGameProfile().getName() + "!")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? PrefixText : "") + "我破坏了白队的床! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("You Break White Bed!"));
                        Break = Break + 1;
                    }
                    if (message.contains("粉队 Bed 被破坏，击杀者： " + mc.thePlayer.getGameProfile().getName() + "!")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? PrefixText : "") + "我破坏了粉队的床! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("You Break Pink Bed!"));
                        Break = Break + 1;
                    }
                    if (message.contains("灰队 Bed 被破坏，击杀者： " + mc.thePlayer.getGameProfile().getName() + "!")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? PrefixText : "") + "我破坏了灰队的床! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("You Break Gray Bed!"));
                        Break = Break + 1;
                    }
                    break;
                case "huayuting":
                    if (message.contains("破坏了红之队 的床!")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + "检测到红队的床被破坏! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("Red Bed Be Break!"));
                        Break = Break + 1;
                    }
                    if (message.contains("破坏了蓝之队 的床!")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + "检测到蓝队的床被破坏! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("Blue Bed Be Break!"));
                        Break = Break + 1;
                    }
                    if (message.contains("破坏了绿之队 的床!")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + "检测到绿队的床被破坏! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("Green Bed Be Break!"));
                        Break = Break + 1;
                    }
                    if (message.contains("破坏了黄之队 的床!")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + "检测到黄队的床被破坏! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("Yellow Bed Be Break!"));
                        Break = Break + 1;
                    }
                    if (message.contains("破坏了紫之队 的床!")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + "检测到紫队的床被破坏! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("Purple Bed Be Break!"));
                        Break = Break + 1;
                    }
                    if (message.contains("破坏了橙之队 的床!")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + "检测到橙队的床被破坏! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("Orange Bed Be Break!"));
                        Break = Break + 1;
                    }
                    if (message.contains("破坏了青之队 的床!")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + "检测到青队的床被破坏! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("Cyan Bed Be Break!"));
                        Break = Break + 1;
                    }
                    if (message.contains("破坏了白之队 的床!")) {
                        mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + "检测到白队的床被破坏! " + (ADValue.get() ? "HDebugQQ群:1128533970" : ""));
                        HDebug.hud.addNotification(new Notification("White Bed Be Break!"));
                        Break = Break + 1;
                    }
                    break;
            }
        }
    }
    @Override
    public String getTag() {
        return ModeValue.get() + " Break:" + Break;
    }
}