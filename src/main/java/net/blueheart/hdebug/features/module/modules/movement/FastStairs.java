package net.blueheart.hdebug.features.module.modules.movement;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.block.BlockUtils;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "FastStairs", description = "Allows you to climb up stairs faster.", category = ModuleCategory.MOVEMENT)
public class FastStairs extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[] {"NCP", "AAC", "LAAC"}, "NCP");
    private final BoolValue longJumpValue = new BoolValue("LongJump", false);

    private boolean canJump;

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (mc.thePlayer == null || HDebug.moduleManager.getModule(Speed.class).getState())
            return;

        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);

        if(mc.thePlayer.onGround && mc.thePlayer.movementInput.moveForward > 0D) {
            final String mode = modeValue.get();

            if(BlockUtils.getBlock(blockPos) instanceof BlockStairs) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.5D, mc.thePlayer.posZ);

                final double motion = mode.equalsIgnoreCase("NCP") ? 1.4D : mode.equalsIgnoreCase("AAC") ? 1.5D : mode.equalsIgnoreCase("AAC") ? 1.499D : 1D;
                mc.thePlayer.motionX *= motion;
                mc.thePlayer.motionZ *= motion;
            }

            if(BlockUtils.getBlock(blockPos.down()) instanceof BlockStairs) {
                mc.thePlayer.motionX *= 1.3D;
                mc.thePlayer.motionZ *= 1.3D;

                if(mode.equalsIgnoreCase("LAAC")) {
                    mc.thePlayer.motionX *= 1.18D;
                    mc.thePlayer.motionZ *= 1.18D;
                }

                canJump = true;
            }else if((mode.equalsIgnoreCase("LAAC") || mode.equalsIgnoreCase("AAC")) && mc.thePlayer.onGround && canJump) {
                if(longJumpValue.get()) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionX *= 1.35D;
                    mc.thePlayer.motionZ *= 1.35D;
                }

                canJump = false;
            }
        }
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
