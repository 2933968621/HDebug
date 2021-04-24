package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.event.Render2DEvent;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.ui.font.FontManager;
import net.blueheart.hdebug.ui.font.UnicodeFontRenderer;
import net.blueheart.hdebug.utils.Logger;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.blueheart.hdebug.value.BoolValue;
import net.minecraft.network.play.server.S02PacketChat;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.blueheart.hdebug.utils.render.RenderUtils.R2DUtils.drawRoundedRect;

@ModuleInfo(name = "ModCheck", description = "Check staff online.", category = ModuleCategory.MISC)
public class ModCheck extends Module {
    private String[] modlist = new String[]{"Sir_Cow",
            "sfarnham",
            "DI4MONDTOOL",
            "Wind1000100",
            "tjbruce17594",
            "jenus_",
            "I_Call_Hacks",
            "geckor",
            "shayk",
            "hammyplaysgames",
            "okexox",
            "pinguin_eater",
            "Flafkas",
            "franz",
            "QuantumBlue",
            "Tiliba",
            "JakeDaaBud",
            "xi6",
            "Jayavarmen",
            "LadyBleu",
            "Mitch_The_Man",
            "DetectiveAndrew",
            "Carcroft",
            "pollefeys",
            "amamimi",
            "mrcraftyketchup",
            "cherry809",
            "Pensul",
            "Herocky"
    };
    public static String[] hackMessage = new String[] { "外挂",
            "黑客",
            "外G",
            "外瓜",
            "挂",
            "能飞",
            "自动攻击",
            "杀戮光环",
            "杀戮",
            "hack",
            "hacker",
            "hax",
            "挂哥",
            "reach",
            "反击退",
            "飞行",
            "卡无敌方块",
            "刷资源",
            "无敌方块",
            "嘴臭",
            "骂人",
            "孤儿",
            "挂逼",
            "挂壁",
            "卡bug",
            "自动搭路",
            "bhopper",
            "bhop",
            "report",
            "loser",
            "noob"
    };
    private String modname;

    private MSTimer timer = new MSTimer();

    private List offlinemod = new ArrayList();

    private List onlinemod = new ArrayList();

    private final BoolValue showOnline = new BoolValue("ShowOnline", true);

    private final BoolValue showOffline = new BoolValue("ShowOffline", true);

    private final BoolValue blockmessage = new BoolValue("BlockWatchdogMessage", true);

    private final BoolValue hackwarning = new BoolValue("HackerWarning", true);

    private final BoolValue debug = new BoolValue("Debug", true);

    private int counter;

    private boolean isFinished;

    @EventTarget
    public void onRender(Render2DEvent e) {
        UnicodeFontRenderer font = FontManager.yahei18;
        List<String> listArray = Arrays.asList(this.modlist);
        int Mod = 0;
        int Long = 0;
        if (showOnline.get())
            for (String mods : listArray) {
                if (onlinemod.contains(mods)) {
                    Long += 10;
                }
            }
        if (showOffline.get() && !onlinemod.isEmpty())
            for (String mods : listArray) {
                if (offlinemod.contains(mods)) {
                    Long += 10;
                }
            }
        if (onlinemod.isEmpty()) {
            Long += 10;
        }
        drawRoundedRect(5.0F, 100.0F, 120.0F, 111.0F, (new Color(0, 125, 255, 255)).getRGB(), (new Color(0, 125, 255, 255)).getRGB());
        drawRoundedRect(5.0F, 110.0F, 120.0F, (110 + Long), (new Color(255, 255, 255, 155)).getRGB(), (new Color(255, 255, 255, 155)).getRGB());
        Long = 0;
        if (showOnline.get())
            for (String mods : listArray) {
                // 如果客服不重复在线
                if (onlinemod.contains(mods)) {
                    // 客服在线 Rect自动加长
                    font.drawStringWithShadow(mods, 15, 110 + Long, Color.GREEN.getRGB());
                    Long += 10;
                    Mod++;// Mod数值增加
                }
            }
        if (showOffline.get() && !onlinemod.isEmpty())
            for (String mods : listArray) {
                // 如果客服不重复在线
                if (offlinemod.contains(mods)) {
                    // 客服在线 Rect自动加长
                    font.drawStringWithShadow(mods, 15, 110 + Long, Color.RED.getRGB());
                    Long += 10;
                    Mod++;// Mod数值增加
                }
            }
        if (Mod < 1)
            if(showOnline.get() || showOffline.get()) {
                font.drawCenteredStringWithShadow("当前没有客服在线", 57.5F, 110.0F, (new Color(75, 75, 75)).getRGB());
            }
        font.drawCenteredStringWithShadow("客服在线列表 (" + Mod + "/" + listArray.size() + ")", 57.5F, 100.0F, (new Color(255, 255, 255)).getRGB());
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat)event.getPacket();
            String e = s02PacketChat.getChatComponent().getUnformattedText();
            if(e.contains("--------------------")){
                if(!debug.get()) {
                    event.cancelEvent();
                }
            }
            if(e.contains("[WATCHDOG]") && blockmessage.get()){
                if(!debug.get()) {
                    event.cancelEvent();
                }
            }
            if (hackwarning.get()) {
                for (String s : hackMessage) {
                    if (e.contains(s.toLowerCase()) && !e.contains("WATCHDOG") && !e.contains("HDebug")) {
                        Logger.printinfo("有人说你是黑客!");
                        break;
                    }
                }
            }
            if (e.contains("玩家离线，你不能邀请") || e.contains("You cannot invite that player since they're not online.")) {
                if(!debug.get()) {
                    event.cancelEvent();
                }
                if (this.onlinemod.contains(this.modname)) {
                    Logger.printinfo(this.modname + "§a已下线!");
                    this.onlinemod.remove(this.modname);
                    this.offlinemod.add(this.modname);
                    return;
                }
                if (!this.offlinemod.contains(this.modname)) {
                    Logger.printinfo(this.modname + "§a不在线!");
                    this.offlinemod.add(this.modname);
                }
            }
            if (e.contains("你不能邀请这位玩家入队") || e.contains("You cannot invite that player.")) {
                if(!debug.get()) {
                    event.cancelEvent();
                }
                if (this.offlinemod.contains(this.modname)) {
                    Logger.printinfo(this.modname + "§a已上线!");
                    this.offlinemod.remove(this.modname);
                    this.onlinemod.add(this.modname);
                    return;
                }
                if (!this.onlinemod.contains(this.modname)) {
                    Logger.printinfo(this.modname + "§a在线!");
                    this.onlinemod.add(this.modname);
                }
            }

            if (e.contains("找不到名为 \"" + this.modname + "\" 的玩家") || e.contains("Couldn't find a player with that name!")) {
                if(!debug.get()) {
                    event.cancelEvent();
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.timer.hasTimePassed(this.isFinished ? 10000L : 2000L)) {
            if (this.counter >= this.modlist.length) {
                this.counter = -1;
                if (!this.isFinished)
                    this.isFinished = true;
            }
            this.counter++;
            this.modname = this.modlist[this.counter];
            mc.thePlayer.sendChatMessage("/p " + this.modname);
            this.timer.reset();
        }
    }
}