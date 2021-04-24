package org.me.ByBlueHeart.HDebugClient.Modules.Movement;

import net.blueheart.hdebug.event.*;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.features.module.modules.movement.LadderJump;
import net.blueheart.hdebug.utils.ClientUtils;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "LongJump", description = "Allows you to jump further.", category = ModuleCategory.MOVEMENT)
public class LongJump extends Module {

    private final ListValue modeValue = new ListValue("Mode", new String[] {"NCP", "RedeSky", "AACv1", "AACv2", "AACv3", "AACv4", "Mineplex", "Mineplex2", "Mineplex3", "HuaYuTingHop"}, "NCP");
    private final FloatValue ncpBoostValue = new FloatValue("NCPBoost", 4.25F, 1F, 10F);
    private final FloatValue jumpMovementValue = new FloatValue("RedeSkyJumpMovementSpeed", 0.15F, 0.01F, 0.5F);
    private final FloatValue motionYValue = new FloatValue("RedeSkyMotionY", 0.05F, 0.01F, 0.1F);

    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private final BoolValue lagback = new BoolValue("LagBackCheck",true);
    private boolean jumped;
    private boolean canBoost;
    private boolean teleported;
    private boolean canMineplexBoost;

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if(LadderJump.jumped)
            MovementUtils.strafe(MovementUtils.getSpeed() * 1.08F);

        if(jumped) {
            final String mode = modeValue.get();

            if (mc.thePlayer.onGround || mc.thePlayer.capabilities.isFlying) {
                jumped = false;
                canMineplexBoost = false;

                if (mode.equalsIgnoreCase("NCP")) {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                return;
            }

            switch (mode.toLowerCase()) {
                case "ncp":
                    MovementUtils.strafe(MovementUtils.getSpeed() * (canBoost ? ncpBoostValue.get() : 1F));
                    canBoost = false;
                    break;
                case "aacv1":
                    mc.thePlayer.motionY += 0.05999D;
                    MovementUtils.strafe(MovementUtils.getSpeed() * 1.08F);
                    break;
                case "aacv4":
                    mc.thePlayer.motionY += 0.05D;
                    MovementUtils.strafe(MovementUtils.getSpeed() * 1.04F);
                    break;
                case "aacv2":
                case "mineplex3":
                    mc.thePlayer.jumpMovementFactor = 0.09F;
                    mc.thePlayer.motionY += 0.0132099999999999999999999999999;
                    mc.thePlayer.jumpMovementFactor = 0.08F;
                    MovementUtils.strafe();
                    break;
                case "aacv3":
                    final EntityPlayerSP player = mc.thePlayer;

                    if(player.fallDistance > 0.5F && !teleported) {
                        double value = 3;
                        EnumFacing horizontalFacing = player.getHorizontalFacing();
                        double x = 0;
                        double z = 0;
                        switch(horizontalFacing) {
                            case NORTH:
                                z = -value;
                                break;
                            case EAST:
                                x = +value;
                                break;
                            case SOUTH:
                                z = +value;
                                break;
                            case WEST:
                                x = -value;
                                break;
                        }

                        player.setPosition(player.posX + x, player.posY, player.posZ + z);
                        teleported = true;
                    }
                    break;
                case "mineplex":
                    mc.thePlayer.motionY += 0.0132099999999999999999999999999;
                    mc.thePlayer.jumpMovementFactor = 0.08F;
                    MovementUtils.strafe();
                    break;
                case "mineplex2":
                    if(!canMineplexBoost)
                        break;

                    mc.thePlayer.jumpMovementFactor = 0.1F;

                    if(mc.thePlayer.fallDistance > 1.5F) {
                        mc.thePlayer.jumpMovementFactor = 0F;
                        mc.thePlayer.motionY = -10F;
                    }
                    MovementUtils.strafe();
                    break;
            }
        }

        if(modeValue.get().equalsIgnoreCase("redesky")){
            mc.thePlayer.jumpMovementFactor = jumpMovementValue.get();
            mc.thePlayer.motionY += motionYValue.get();
        }

        if(autoJumpValue.get() && mc.thePlayer.onGround && MovementUtils.isMoving()) {
            jumped = true;
            mc.thePlayer.jump();
        }
    }

    @EventTarget
    public void onMove(final MoveEvent event) {
        final String mode = modeValue.get();

        if (mode.equalsIgnoreCase("mineplex3")) {
            if(mc.thePlayer.fallDistance != 0)
                mc.thePlayer.motionY += 0.037;
        } else if (mode.equalsIgnoreCase("ncp") && !MovementUtils.isMoving() && jumped) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
            event.zeroXZ();
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        jumped = true;
        canBoost = true;
        teleported = false;

        if(getState()) {
            switch(modeValue.get().toLowerCase()) {
                case "mineplex":
                    event.setMotion(event.getMotion() * 4.08f);
                    break;
                case "mineplex2":
                    if(mc.thePlayer.isCollidedHorizontally) {
                        event.setMotion(2.31f);
                        canMineplexBoost = true;
                        mc.thePlayer.onGround = false;
                    }
                    break;
            }
        }

    }
    @EventTarget
    public void onPacket(final PacketEvent event){
        final Packet<?> packet = event.getPacket();
        if(packet instanceof S08PacketPlayerPosLook){
            if(lagback.get()){
                ClientUtils.displayChatMessage("§8[§c§lLagBackCheck§8] §cSetback detected.");
                this.toggle();
            }
        }
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
