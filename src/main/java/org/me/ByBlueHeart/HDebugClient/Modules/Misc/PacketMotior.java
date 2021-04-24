package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.Logger;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "PacketMonitor", description = "Check C03Packet.", category = ModuleCategory.MISC)
public class PacketMotior extends Module {
    private int packetcounter;
    private String counter = "";
    private MSTimer timer = new MSTimer();

    @EventTarget
    public void onPacket(PacketEvent e){
        if (e.getPacket() instanceof C03PacketPlayer) {
            ++packetcounter;
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent e){
        if (timer.delay(1000L)) {
            if (packetcounter > 22) {
                Logger.printinfo("\247c警告! Packet发送数量不正常! Packet数量:" + packetcounter);
            }
            counter = String.valueOf(packetcounter);
            packetcounter = 0;
            timer.reset();
        }
    }
    @Override
    public String getTag() {
        return packetcounter+"";
    }
}
