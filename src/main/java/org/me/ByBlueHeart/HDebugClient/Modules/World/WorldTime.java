package org.me.ByBlueHeart.HDebugClient.Modules.World;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.IntegerValue;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@ModuleInfo(name = "WorldTime", description = "Change your World Time.", category = ModuleCategory.WORLD)
public class WorldTime extends Module {
    private final IntegerValue time = new IntegerValue("Time", 13000, 0, 24000);

    @EventTarget
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof S03PacketTimeUpdate)
            e.cancelEvent();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc.theWorld.setWorldTime(this.time.get());
    }
}