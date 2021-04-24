package org.me.ByBlueHeart.HDebugClient.Modules.Player;

import net.blueheart.hdebug.event.*;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;
import net.blueheart.hdebug.utils.block.BlockUtils;
import net.blueheart.hdebug.utils.timer.TickTimer;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "Phase", description = "Allows you to walk through blocks.", category = ModuleCategory.PLAYER)
public class Phase extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[] {"Vanilla", "Skip", "Spartan", "Clip", "AAC3.5.0", "Mineplex", "FullBlock"}, "Vanilla");

    private final TickTimer tickTimer = new TickTimer();

    private boolean mineplexClip;
    private TickTimer mineplexTickTimer = new TickTimer();

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        final boolean isInsideBlock = BlockUtils.collideBlockIntersects(mc.thePlayer.getEntityBoundingBox(), block -> !(block instanceof BlockAir));

        if(isInsideBlock && !modeValue.get().equalsIgnoreCase("Mineplex")) {
            mc.thePlayer.noClip = true;
            mc.thePlayer.motionY = 0D;
            mc.thePlayer.onGround = true;
        }

        final NetHandlerPlayClient netHandlerPlayClient = mc.getNetHandler();

        switch(modeValue.get().toLowerCase()) {
            case "vanilla": {
                if(!mc.thePlayer.onGround || !tickTimer.hasTimePassed(2) || !mc.thePlayer.isCollidedHorizontally || !(!isInsideBlock || mc.thePlayer.isSneaking()))
                    break;

                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.5D, 0, 0.5D, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.5D, 0, 0.5D, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 0.5D, mc.thePlayer.posY, mc.thePlayer.posZ + 0.5D, true));
                final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                final double x = -Math.sin(yaw) * 0.04D;
                final double z = Math.cos(yaw) * 0.04D;
                mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                tickTimer.reset();
                break;
            }
            case "skip": {
                if(!mc.thePlayer.onGround || !tickTimer.hasTimePassed(2) || !mc.thePlayer.isCollidedHorizontally || !(!isInsideBlock || mc.thePlayer.isSneaking()))
                    break;

                final double direction = MovementUtils.getDirection();
                final double posX = -Math.sin(direction) * 0.3;
                final double posZ = Math.cos(direction) * 0.3;

                for(int i = 0; i < 3; ++i) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06, mc.thePlayer.posZ, true));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + posX * i, mc.thePlayer.posY, mc.thePlayer.posZ + posZ * i, true));
                }

                mc.thePlayer.setEntityBoundingBox(mc.thePlayer.getEntityBoundingBox().offset(posX, 0.0D, posZ));
                mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX + posX, mc.thePlayer.posY, mc.thePlayer.posZ + posZ);
                tickTimer.reset();
                break;
            }
            case "spartan": {
                if(!mc.thePlayer.onGround || !tickTimer.hasTimePassed(2) || !mc.thePlayer.isCollidedHorizontally || !(!isInsideBlock || mc.thePlayer.isSneaking()))
                    break;

                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.5D, 0, 0.5D, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.2D, mc.thePlayer.posZ, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.5D, 0, 0.5D, true));
                netHandlerPlayClient.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 0.5D, mc.thePlayer.posY, mc.thePlayer.posZ + 0.5D, true));
                final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                final double x = -Math.sin(yaw) * 0.04D;
                final double z = Math.cos(yaw) * 0.04D;
                mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                tickTimer.reset();
                break;
            }
            case "clip": {
                if(!tickTimer.hasTimePassed(2) || !mc.thePlayer.isCollidedHorizontally || !(!isInsideBlock || mc.thePlayer.isSneaking()))
                    break;

                final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                final double oldX = mc.thePlayer.posX;
                final double oldZ = mc.thePlayer.posZ;

                for(int i = 1; i <= 10; i++) {
                    final double x = -Math.sin(yaw) * i;
                    final double z = Math.cos(yaw) * i;

                    if(BlockUtils.getBlock(new BlockPos(oldX + x, mc.thePlayer.posY, oldZ + z)) instanceof BlockAir && BlockUtils.getBlock(new BlockPos(oldX + x, mc.thePlayer.posY + 1, oldZ + z)) instanceof BlockAir) {
                        mc.thePlayer.setPosition(oldX + x, mc.thePlayer.posY, oldZ + z);
                        break;
                    }
                }
                tickTimer.reset();
                break;
            }
            case "aac3.5.0": {
                if(!tickTimer.hasTimePassed(2) || !mc.thePlayer.isCollidedHorizontally || !(!isInsideBlock || mc.thePlayer.isSneaking()))
                    break;

                final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                final double oldX = mc.thePlayer.posX;
                final double oldZ = mc.thePlayer.posZ;
                final double x = -Math.sin(yaw);
                final double z = Math.cos(yaw);

                mc.thePlayer.setPosition(oldX + x, mc.thePlayer.posY, oldZ + z);
                tickTimer.reset();
                break;
            }
            case "fullblock": {
                if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && !isInsideBlock) {
                    final double x = mc.thePlayer.movementInput.moveForward * 0.4 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)) + mc.thePlayer.movementInput.moveStrafe * 0.4 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
                    final double z = mc.thePlayer.movementInput.moveForward * 0.4 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)) - mc.thePlayer.movementInput.moveStrafe * 0.4 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
                    for (int i = 1; i < 11; ++i) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, Double.MAX_VALUE * i, mc.thePlayer.posZ, false));

                    }
                    final double posX = mc.thePlayer.posX;
                    final double posY = mc.thePlayer.posY;
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY - (isOnLiquid() ? 9000.0 : 0.1), mc.thePlayer.posZ, false));
                        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                    break;
                }else if(isInsideBlock){
                    final double x = mc.thePlayer.movementInput.moveForward * 0.4 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)) + mc.thePlayer.movementInput.moveStrafe * 0.4 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
                    final double z = mc.thePlayer.movementInput.moveForward * 0.4 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)) - mc.thePlayer.movementInput.moveStrafe * 0.4 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
                        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                }
                break;
            }
        }

        tickTimer.update();
    }
    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox();

        if (boundingBox == null) {
            return false;
        } else {
            boundingBox = boundingBox.contract(0.01D, 0.0D, 0.01D).offset(0.0D, -0.01D, 0.0D);
            boolean onLiquid = false;
            int y = (int) boundingBox.minY;

            for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0D); ++x) {
                for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0D); ++z) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != Blocks.air) {
                        if (!(block instanceof BlockLiquid)) {
                            return false;
                        }

                        onLiquid = true;
                    }
                }
            }

            return onLiquid;
        }
    }
    @EventTarget
    public void onBlockBB(final BlockBBEvent event) {
        if(mc.thePlayer != null && BlockUtils.collideBlockIntersects(mc.thePlayer.getEntityBoundingBox(), block -> !(block instanceof BlockAir)) && event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.getEntityBoundingBox().minY && !modeValue.get().equalsIgnoreCase("Mineplex")) {
            final AxisAlignedBB axisAlignedBB = event.getBoundingBox();

            event.setBoundingBox(new AxisAlignedBB(axisAlignedBB.maxX, mc.thePlayer.getEntityBoundingBox().minY, axisAlignedBB.maxZ, axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ));
        }
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if(packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;

            if(modeValue.get().equalsIgnoreCase("AAC3.5.0")) {
                final float yaw = (float) MovementUtils.getDirection();

                packetPlayer.x = packetPlayer.x - MathHelper.sin(yaw) * 0.00000001D;
                packetPlayer.z = packetPlayer.z + MathHelper.cos(yaw) * 0.00000001D;
            }
        }
    }

    @EventTarget
    private void onMove(final MoveEvent event) {
        if (modeValue.get().equalsIgnoreCase("mineplex")) {
            if (mc.thePlayer.isCollidedHorizontally)
                mineplexClip = true;
            if (!mineplexClip)
                return;

            mineplexTickTimer.update();

            event.setX(0);
            event.setZ(0);

            if (mineplexTickTimer.hasTimePassed(3)) {
                mineplexTickTimer.reset();
                mineplexClip = false;
            } else if (mineplexTickTimer.hasTimePassed(1)) {
                final double offset = mineplexTickTimer.hasTimePassed(2) ? 1.6D : 0.06D;
                final double direction = MovementUtils.getDirection();

                mc.thePlayer.setPosition(mc.thePlayer.posX + (-Math.sin(direction) * offset), mc.thePlayer.posY, mc.thePlayer.posZ + (Math.cos(direction) * offset));
            }
        }
    }

    @EventTarget
    public void onPushOut(PushOutEvent event) {
        event.cancelEvent();
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
