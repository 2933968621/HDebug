package net.blueheart.hdebug.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.me.ByBlueHeart.HDebugClient.Modules.Player.NoFall;

public class NoFallUtil {
    public static void Hypixel() {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        if(NoFall.FallDistance.get()) {
            Minecraft.getMinecraft().thePlayer.fallDistance = 0;
        }
    }
}
