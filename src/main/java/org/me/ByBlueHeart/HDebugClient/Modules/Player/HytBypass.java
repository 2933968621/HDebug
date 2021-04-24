package org.me.ByBlueHeart.HDebugClient.Modules.Player;

import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.WorldEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.BoolValue;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@ModuleInfo(name = "HytBypass", description = "Bypass HuaYuTing 16#22#32 Banned", category = ModuleCategory.PLAYER)
public class HytBypass extends Module {
    public BoolValue newtest = new BoolValue("1.12.2",false);
    @EventTarget
    public void onWorld(final WorldEvent e) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("base64", "ecbeb575677ab9a37410748a5f429f9f");
                jsonObject.addProperty("cltitle", "\u6211\u7684\u4e16\u754c " + (newtest.get() ? "1.12.2" : "1.8.9"));
                jsonObject.addProperty("isLiquidbounce", false);
                jsonObject.addProperty("path", "mixins.mcwrapper.json");
                jsonObject.addProperty("player", mc.thePlayer.getName());

                PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                packetBuffer.writeString(jsonObject.toString());
                C17PacketCustomPayload antiCheat = new C17PacketCustomPayload("AntiCheat", packetBuffer);
                mc.getNetHandler().getNetworkManager().sendPacket(antiCheat);
                System.out.println(jsonObject.toString());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}