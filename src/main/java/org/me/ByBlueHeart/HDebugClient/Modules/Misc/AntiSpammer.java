package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.BoolValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

@ModuleInfo(name = "AntiSpammer", description = "Auto Bypass Spammer.", category = ModuleCategory.MISC)
public class AntiSpammer extends Module {
    int Anti = 0;
    private final BoolValue GroupValue = new BoolValue("QQGroup", true);
    private final BoolValue BoxValue = new BoolValue("Box", true);
    private final BoolValue ConfigValue = new BoolValue("Config", true);
    private final BoolValue BuyValue = new BoolValue("Buy", true);
    private final BoolValue AbuseValue = new BoolValue("Abuse", true);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);
    private final BoolValue LValue = new BoolValue("L", true);
    private final BoolValue ClientValue = new BoolValue("Client", true);
    private final BoolValue NameValue = new BoolValue("Name", true);
    private final BoolValue GGValue = new BoolValue("GG", true);
    private final BoolValue AntiAntiGodPlayerValue = new BoolValue("AntiAntiGodPlayerMessageBypass", true);

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            if (message.contains("群") || message.contains("链接")) {
                if (GroupValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("盒子") || message.contains("Box") || message.contains("工具箱")) {
                if (BoxValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("配置") || message.contains("Config")) {
                if (ConfigValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("付费") || message.contains("买") || message.contains("Buy") || message.contains("Pay") || message.contains("小卖部") || message.contains("卡网") || message.contains("anfaka") || message.contains("安发卡") || message.contains("内部")) {
                if (BuyValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("fw") || message.contains("废物") || message.contains("nm") || message.contains("你妈") || message.contains("你妈死了") || message.contains("NMSL") || message.contains("NM$L") || message.contains("wnf") || message.contains("窝囊废") || message.contains("扣子") || message.contains("殴打")  || message.contains("造极巅峰") || message.contains("气急败坏") || message.contains("暴打") || message.contains("Loser") || message.contains("小学生") || message.contains("xxs") || message.contains("婊子妈") || message.contains("户籍") || message.contains("他妈") || message.contains("TM") || message.contains("™") || message.contains("SB") || message.contains("傻逼") || message.contains("↑↓") || message.contains("脑残") || message.contains("NC")) {
                if (AbuseValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("胖胖") || message.contains("Pang") || message.contains("SMG") || message.contains("getdown") || message.contains("Coal") || message.contains("金奇")|| message.contains("SpaceKing") || message.contains("MCWG") || message.contains("RSX") || message.contains("CoCoA") || message.contains("Arda") || message.contains("Easy")) {
                if (PrefixValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("L")) {
                if (LValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("LiquidBounce") || message.contains("LB") || message.contains("水影") || message.contains("skidsense") || message.contains("Debug") || message.contains("AzureWare") || message.contains("Sigma") || message.contains("Jello") || message.contains("Flux") || message.contains("Power") || message.contains("Hanabi") || message.contains("HanFia") || message.contains("StormBounce") || message.contains("Nov") || message.contains("Novo") || message.contains("NovOline") || message.contains("Vape") || message.contains("ETB") || message.contains("Leain") || message.contains("Jigsaw") || message.contains("LowClientBounce") || message.contains("ExcessiveKill") || message.contains("Remix") || message.contains("LAC") || message.contains("Fantasiesh") || message.contains("Exhibition") || message.contains("BW") || message.contains("Nabla") || message.contains("SkyFall") || message.contains("IDBUG") || message.contains("Client")) {
                if (ClientValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("Ax") || message.contains("AlanXiao") || message.contains("Margele") || message.contains("Loyisa") || message.contains("镜光") || message.contains("LEF") || message.contains("Coke") || message.contains("lnk") || message.contains("上学困难户") || message.contains("Ho3") || message.contains("SK") || message.contains("ChaoJi") || message.contains("Storm") || message.contains("暴风") || message.contains("Bosten") || message.contains("白曦") || message.contains("神帝") || message.contains("浪子") || message.contains("天文") || message.contains("明儿") || message.contains("Minger") || message.contains("Aqua") || message.contains("Rice") || message.contains("白米") || message.contains("李佳乐") || message.contains("陈晓峰") || message.contains("白治军") || message.contains("12shou") || message.contains("婷儿") || message.contains("XruiDD") || message.contains("方程") || message.contains("CCBlueX") || message.contains("污点") || message.contains("Soulplexis") || message.contains("SuperSkidder") || message.contains("MoShen") || message.contains("星空") || message.contains("Ikaros") || message.contains("凉屿") || message.contains("孤傲") || message.contains("Duang") || message.contains("DecayEvent") || message.contains("清风") || message.contains("天义") || message.contains("SiNue") || message.contains("肆虐") || message.contains("ProdiGai") || message.contains("单恋穿越") || message.contains("雪花") || message.contains("Tuza") || message.contains("少轻狂") || message.contains("雷级") || message.contains("Dertarer_NPC") || message.contains("Moliser") || message.contains("梁诺言") || message.contains("快乐黄鸟") || message.contains("惜别") || message.contains("兮梦") || message.contains("惜梦") || message.contains("胖虎") || message.contains("玖玖") || message.contains("陈荣钦") || message.contains("梁诺言") || message.contains("梁嘉欣")) {
                if (NameValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("GG")) {
                if (GGValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
            if (message.contains("起床战争 >> *你*[?20] (你母的队伍) 杀死了 " + mc.thePlayer.getGameProfile().getName() + "(你母的队伍)")) {
                if (AntiAntiGodPlayerValue.get()) {
                    event.cancelEvent();
                    Anti = Anti + 1;
                }
            }
        }
    }
    @Override
    public String getTag() {
        return "Anti:" + Anti;
    }
}