package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.Helper;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.IntegerValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import org.me.ByBlueHeart.HDebugClient.HDebugMain;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@ModuleInfo(name = "AutoGG", description = "RoundBegan Say Game Start.", category = ModuleCategory.MISC)
public class AutoGG extends Module {
    int Win = 0;
    private Timer timer = new Timer();
    private final ListValue ModeValue = new ListValue("Mode", new String[]{"Hypixel", "HuaYuTing"}, "Hypixel");
    private final ListValue MusicKitMode = new ListValue("MusicKitMode", new String[]{"FrameOfMind", "Bodacious", "EyeDragon", "BlackHuman"}, "FrameOfMind");
    private final ListValue AutoJoinModeValue = new ListValue("AutoJoinMode", new String[]{"BedWars_1v1", "BedWars_2v2", "BedWars_3v3","BedWars_4v4", "SkyWars_Solo", "SkyWars_Solo_Insane","SkyWars_Solo_LuckyBlock","SkyWars_Team","SkyWars_Team_Insane","SkyWars_Team_LuckyBlock","SurivialGames_Solo","SurivialGames_Team","MiniWalls"}, "BedWars_4v4");
    private final IntegerValue delay = new IntegerValue("AutoJoinTimer", 3, 0, 10);
    private final BoolValue AutoJoinValue = new BoolValue("AutoJoin", true);
    private final BoolValue ADValue = new BoolValue("AD", true);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);
    private final BoolValue WIN = new BoolValue("WIN", true);
    private final BoolValue MVP = new BoolValue("MVP", true);
    private final BoolValue DIED = new BoolValue("DIED", true);

    @EventTarget
    public void onPacket(final PacketEvent event) throws IOException {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String AD = " HDebugQQ群:1128533970";
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            switch (ModeValue.get().toLowerCase()) {
                case "hypixel":
                    if (message.contains("胜利者") || message.contains("Winner")) {
                        mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? "[HDebug]" : "") + "GG" + (ADValue.get() ? AD : ""));
                        Win = Win + 1;
                        this.timer.schedule(new TimerTask() {
                            public void run() {
                                switch (AutoJoinModeValue.get().toLowerCase()) {
                                    case "bedwars_1v1":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play bedwars_eight_one");
                                        }
                                        break;
                                    case "bedwars_2v2":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play bedwars_eight_two");
                                        }
                                        break;
                                    case "bedwars_3v3":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play bedwars_four_three");
                                        }
                                        break;
                                    case "bedwars_4v4":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play bedwars_four_four");
                                        }
                                        break;
                                    case "skywars_solo":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play solo_normal");
                                        }
                                        break;
                                    case "skywars_solo_insane":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play solo_insane");
                                        }
                                        break;
                                    case "skywars_solo_luckyblock":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play solo_insane_lucky");
                                        }
                                        break;
                                    case "skywars_teams":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play teams_normal");
                                        }
                                        break;
                                    case "skywars_teams_insane":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play teams_insane");
                                        }
                                        break;
                                    case "skywars_team_luckyblock":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play teams_insane_lucky");
                                        }
                                        break;
                                    case "surivialgames_solo":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play blitz_solo_normal");
                                        }
                                        break;
                                    case "surivialgames_team":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play blitz_teams_normal");
                                        }
                                        break;
                                    case "miniwalls":
                                        if (AutoJoinValue.get()) {
                                            mc.thePlayer.sendChatMessage("/play arcade_mini_walls");
                                        }
                                        break;
                                }
                            }
                        }, (delay.get().longValue() * 10) * 100);
                    }
                    break;
                case "huayuting":
                    if (message.contains("获得胜利!")) {
                        mc.thePlayer.sendChatMessage((PrefixValue.get() ? "[HDebug]" : "") + "GG" + (ADValue.get() ? AD : ""));
                        Win = Win + 1;
                    }
                    break;
            }
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent e){
        if(WIN.get() && HDebugMain.BuyFrameOfMind != true && AutoJoinModeValue.get() == "FrameOfMind"){
            Helper.sendMessage("> Error You No Buy MusicKit FrameOfMind");
            WIN.set(false);
        }
        if(DIED.get() && HDebugMain.BuyFrameOfMind != true && AutoJoinModeValue.get() == "FrameOfMind"){
            Helper.sendMessage("> Error You No Buy MusicKit FrameOfMind");
            DIED.set(false);
        }
        if(MVP.get() && HDebugMain.BuyFrameOfMind != true && AutoJoinModeValue.get() == "FrameOfMind"){
            Helper.sendMessage("> Error You No Buy MusicKit FrameOfMind");
            MVP.set(false);
        }
        if(WIN.get() && HDebugMain.BuyBodacious != true && AutoJoinModeValue.get() == "Bodacious"){
            Helper.sendMessage("> Error You No Buy MusicKit Bodacious");
            WIN.set(false);
        }
        if(DIED.get() && HDebugMain.BuyBodacious != true && AutoJoinModeValue.get() == "Bodacious"){
            Helper.sendMessage("> Error You No Buy MusicKit Bodacious");
            DIED.set(false);
        }
        if(MVP.get() && HDebugMain.BuyBodacious != true && AutoJoinModeValue.get() == "Bodacious"){
            Helper.sendMessage("> Error You No Buy MusicKit Bodacious");
            MVP.set(false);
        }
        if(WIN.get() && HDebugMain.BuyEyeDragon != true && AutoJoinModeValue.get() == "EyeDragon"){
            Helper.sendMessage("> Error You No Buy MusicKit EyeDragon");
            WIN.set(false);
        }
        if(DIED.get() && HDebugMain.BuyEyeDragon != true && AutoJoinModeValue.get() == "EyeDragon"){
            Helper.sendMessage("> Error You No Buy MusicKit EyeDragon");
            DIED.set(false);
        }
        if(MVP.get() && HDebugMain.BuyEyeDragon != true && AutoJoinModeValue.get() == "EyeDragon"){
            Helper.sendMessage("> Error You No Buy MusicKit EyeDragon");
            MVP.set(false);
        }
        if(WIN.get() && HDebugMain.BuyBlackHuman != true && AutoJoinModeValue.get() == "BlackHuman"){
            Helper.sendMessage("> Error You No Buy MusicKit BlackHuman");
            WIN.set(false);
        }
        if(DIED.get() && HDebugMain.BuyBlackHuman != true && AutoJoinModeValue.get() == "BlackHuman"){
            Helper.sendMessage("> Error You No Buy MusicKit BlackHuman");
            DIED.set(false);
        }
        if(MVP.get() && HDebugMain.BuyBlackHuman != true && AutoJoinModeValue.get() == "BlackHuman"){
            Helper.sendMessage("> Error You No Buy MusicKit BlackHuman");
            MVP.set(false);
        }
    }
    @Override
    public String getTag() {
        return ModeValue.get() + (WIN.get() || MVP.get() || DIED.get() ? " - " + MusicKitMode.get() + " - Win:" : " - Win:") + Win;
    }
}