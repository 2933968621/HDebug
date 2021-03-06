package org.me.ByBlueHeart.HDebugClient.Modules.Player;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.MoveEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.Flight;

@ModuleInfo(name = "AntiFall", description = "Antifall.", category = ModuleCategory.PLAYER)
public class AntiFall extends Module {
    private final BoolValue voidcheck = new BoolValue("Void", true);
    private final FloatValue distance = new FloatValue("FallDistance", 5F, 1F, 20F);
    MSTimer timer = new MSTimer();
    private boolean saveMe;

    @EventTarget
    public void onMove(MoveEvent event) {
        if ((saveMe && timer.delay(150)) || mc.thePlayer.isCollidedVertically) {
            saveMe = false;
            timer.reset();
        }
        float dist = distance.get();
        if (mc.thePlayer.fallDistance > dist && !HDebug.moduleManager.getModule(Flight.class).getState()) {
            if (!voidcheck.get() || !isBlockUnder()) {
                if (!saveMe) {
                    saveMe = true;
                    timer.reset();
                }
                mc.thePlayer.fallDistance = 0;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 12, mc.thePlayer.posZ, false));
            }
        }
    }
    private boolean isBlockUnder() {
        if(mc.thePlayer.posY < 0)
            return false;
        for(int off = 0; off < (int)mc.thePlayer.posY+2; off += 2){
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -off, 0);
            if(!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()){
                return true;
            }
        }
        return false;
    }
}
