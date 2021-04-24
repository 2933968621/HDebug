package org.me.ByBlueHeart.HDebugClient.Modules.Movement;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.KeyEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.blueheart.hdebug.value.IntegerValue;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.ChatComponentText;
;

@ModuleInfo(name = "AirHighJump", description = "Use HighJump in Air.", category = ModuleCategory.MOVEMENT)
public class AirHighJump extends Module {

    private final IntegerValue HighValue = new IntegerValue("High", 10, 0, 10);

    @Override
    public void onEnable() {
        mc.thePlayer.addChatMessage(new ChatComponentText("§c请按下空格来执行高跳"));
    }

    @EventTarget
    public void onKey(KeyEvent e) {
        if (e.getKey() == 57) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + HighValue.get(), mc.thePlayer.posZ, true));
            MovementUtils.forward(0.04D);
        }
    }
}