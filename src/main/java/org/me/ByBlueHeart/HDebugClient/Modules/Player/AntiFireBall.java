package org.me.ByBlueHeart.HDebugClient.Modules.Player;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.ClientUtils;
import net.blueheart.hdebug.utils.RotationUtil;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
;

@ModuleInfo(name = "AntiFireBall", description = "AntiFireBall.", category = ModuleCategory.PLAYER)
public class AntiFireBall extends Module {
    private final FloatValue range = new FloatValue("Range", 4.5F, 1.0F, 6.0F);
    private final BoolValue rot = new BoolValue("Rot", true);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityFireball)) continue;
            double rangeToEntity = mc.thePlayer.getDistanceToEntity(entity);
            if (!(rangeToEntity <= this.range.get())) continue;
            mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
            if (!((Boolean)this.rot.get()).booleanValue()) continue;
            float[] rotation = RotationUtil.getRotations((Entity)entity);
            // Location.setYaw(rotation[0]);
            // Location.setPitch(rotation[1]);
        }
    }
}
