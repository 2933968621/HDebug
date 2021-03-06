
package net.blueheart.hdebug.features.module.modules.movement;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.block.BlockUtils;
import net.blueheart.hdebug.value.FloatValue;
import net.minecraft.block.BlockLiquid;

@ModuleInfo(name = "WaterSpeed", description = "Allows you to swim faster.", category = ModuleCategory.MOVEMENT)
public class WaterSpeed extends Module {

    private final FloatValue speedValue = new FloatValue("Speed", 1.2F, 1.1F, 1.5F);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(mc.thePlayer.isInWater() && BlockUtils.getBlock(mc.thePlayer.getPosition()) instanceof BlockLiquid) {
            final float speed = speedValue.get();

            mc.thePlayer.motionX *= speed;
            mc.thePlayer.motionZ *= speed;
        }
    }
}