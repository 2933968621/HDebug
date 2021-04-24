package org.me.ByBlueHeart.HDebugClient.Modules.Player;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.*;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.features.module.modules.render.FreeCam;
import net.blueheart.hdebug.utils.NoFallUtil;
import net.blueheart.hdebug.utils.RotationUtils;
import net.blueheart.hdebug.utils.VecRotation;
import net.blueheart.hdebug.utils.block.BlockUtils;
import net.blueheart.hdebug.utils.misc.FallingPlayer;
import net.blueheart.hdebug.utils.timer.TickTimer;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;

@ModuleInfo(name = "NoFall", description = "Prevents you from taking fall damage.", category = ModuleCategory.PLAYER)
public class NoFall extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"SpoofGround", "NoGround", "Packet", "MLG", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "AACLatest", "Spartan", "CubeCraft", "Hypixel", "AAC4"}, "SpoofGround");
    public static final BoolValue FallDistance = new BoolValue("HypixelFallDistance", false);
    private final FloatValue minFallDistance = new FloatValue("MinMLGHeight", 5F, 2F, 50F);
    private final TickTimer spartanTimer = new TickTimer();
    private final TickTimer mlgTimer = new TickTimer();
    private int state;
    private int hypixel;
    private boolean jumped;
    private VecRotation currentMlgRotation;
    private int currentMlgItemIndex;
    private BlockPos currentMlgBlock;
    private Packet packets;

    public boolean isVoid() {
        if(mc.thePlayer.posY < 0)
            return false;
        for(int off = 0; off < (int)mc.thePlayer.posY+2; off += 2) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -off, 0);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @EventTarget(ignoreCondition = true)
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.onGround)
            jumped = false;

        if (mc.thePlayer.motionY > 0)
            jumped = true;

        if (!getState() || HDebug.moduleManager.getModule(FreeCam.class).getState())
            return;

        if (BlockUtils.collideBlock(mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) ||
                BlockUtils.collideBlock(new AxisAlignedBB(mc.thePlayer.getEntityBoundingBox().maxX, mc.thePlayer.getEntityBoundingBox().maxY, mc.thePlayer.getEntityBoundingBox().maxZ, mc.thePlayer.getEntityBoundingBox().minX, mc.thePlayer.getEntityBoundingBox().minY - 0.01D, mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid))
            return;

        switch (modeValue.get().toLowerCase()) {
            case "packet":
                if (mc.thePlayer.fallDistance > 2F)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                break;
            case "aac":
                if (mc.thePlayer.fallDistance > 2F) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    state = 2;
                } else if (state == 2 && mc.thePlayer.fallDistance < 2) {
                    mc.thePlayer.motionY = 0.1D;
                    state = 3;
                    return;
                }

                switch (state) {
                    case 3:
                        mc.thePlayer.motionY = 0.1D;
                        state = 4;
                        break;
                    case 4:
                        mc.thePlayer.motionY = 0.1D;
                        state = 5;
                        break;
                    case 5:
                        mc.thePlayer.motionY = 0.1D;
                        state = 1;
                        break;
                }
                break;
            case "laac":
                if (!jumped && mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater()
                        && !mc.thePlayer.isInWeb)
                    mc.thePlayer.motionY = -6;
                break;
            case "aac3.3.11":
                if (mc.thePlayer.fallDistance > 2) {
                    mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY - 10E-4D, mc.thePlayer.posZ, mc.thePlayer.onGround));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
                break;
            case "aac3.3.15":
                if (mc.thePlayer.fallDistance > 2) {
                    if (!mc.isIntegratedServerRunning())
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                Double.NaN, mc.thePlayer.posZ, false));
                    mc.thePlayer.fallDistance = -9999;
                }
                break;
            case "spartan":
                spartanTimer.update();

                if (mc.thePlayer.fallDistance > 1.5 && spartanTimer.hasTimePassed(10)) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + 10, mc.thePlayer.posZ, true));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY - 10, mc.thePlayer.posZ, true));
                    spartanTimer.reset();
                }
                break;
        }
    }

    @EventTarget
    public void onPacket(final PacketEvent event) {
        final Packet<?> packet = event.getPacket();
        final String mode = modeValue.get();
        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer playerPacket = (C03PacketPlayer) packet;

            if (mode.equalsIgnoreCase("AAC4")){
                playerPacket.onGround = true;
                packets = packet;
            }

            if (mode.equalsIgnoreCase("SpoofGround"))
                playerPacket.onGround = true;

            if (mode.equalsIgnoreCase("NoGround"))
                playerPacket.onGround = false;

            if (mode.equalsIgnoreCase("AACLatest"))
                playerPacket.onGround = false;

            if(mode.equalsIgnoreCase("Hypixel")){
                if (mc.thePlayer.fallDistance >= 2 ) {
                    if (playerPacket.isMoving()) {
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer(true));
                    } else {
                        playerPacket.onGround = true;
                    }
                    mc.thePlayer.fall(1, 0);
                }
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (BlockUtils.collideBlock(mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(mc.thePlayer.getEntityBoundingBox().maxX, mc.thePlayer.getEntityBoundingBox().maxY, mc.thePlayer.getEntityBoundingBox().maxZ, mc.thePlayer.getEntityBoundingBox().minX, mc.thePlayer.getEntityBoundingBox().minY - 0.01D, mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid))
            return;

        if (modeValue.get().equalsIgnoreCase("laac")) {
            if (!jumped && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInWeb && mc.thePlayer.motionY < 0D) {
                event.setX(0);
                event.setZ(0);
            }
        }
    }

    @EventTarget
    private void onMotion(MotionEvent event) {
        if(modeValue.get() == "AAC4"){
            if(event.getEventState() == EventState.PRE){
                boolean fakelag;
                if (!inVoid()) {
                    mc.thePlayer.sendQueue.addToSendQueue(packets);
                }
            }
        }
        if(modeValue.get() == "MLG"){
            if (event.getEventState() == EventState.PRE) {
                currentMlgRotation = null;
                mlgTimer.update();

                if (!mlgTimer.hasTimePassed(10))
                    return;

                if (mc.thePlayer.fallDistance > minFallDistance.get()) {
                    FallingPlayer fallingPlayer = new FallingPlayer(
                            mc.thePlayer.posX,
                            mc.thePlayer.posY,
                            mc.thePlayer.posZ,
                            mc.thePlayer.motionX,
                            mc.thePlayer.motionY,
                            mc.thePlayer.motionZ,
                            mc.thePlayer.rotationYaw,
                            mc.thePlayer.moveStrafing,
                            mc.thePlayer.moveForward
                    );

                    double maxDist = mc.playerController.getBlockReachDistance() + 1.5;

                    FallingPlayer.CollisionResult collision = fallingPlayer.findCollision((int) Math.ceil((1.0 / mc.thePlayer.motionY) * (-maxDist)));

                    if (collision == null)
                        return;

                    boolean ok = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.eyeHeight, mc.thePlayer.posZ).distanceTo(new Vec3(collision.getPos()).addVector(0.5, 0.5, 0.5)) < mc.playerController.getBlockReachDistance() + Math.sqrt(0.75);

                    if (mc.thePlayer.motionY < (collision.getPos().getY() + 1) - mc.thePlayer.posY) {
                        ok = true;
                    }

                    if (!ok)
                        return;

                    int index = -1;

                    for (int i = 36; i < 45; i++) {
                        ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                        if (itemStack != null && (itemStack.getItem() == Items.water_bucket || itemStack.getItem() instanceof ItemBlock && ((ItemBlock) itemStack.getItem()).getBlock() == Blocks.web)) {
                            index = i - 36;

                            if (mc.thePlayer.inventory.currentItem == index)
                                break;
                        }
                    }

                    if (index == -1)
                        return;

                    currentMlgItemIndex = index;
                    currentMlgBlock = collision.getPos();

                    if (mc.thePlayer.inventory.currentItem != index) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(index));
                    }

                    currentMlgRotation = RotationUtils.faceBlock(collision.getPos());
                    currentMlgRotation.getRotation().toPlayer(mc.thePlayer);
                }
            } else if (currentMlgRotation != null) {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(currentMlgItemIndex + 36).getStack();

                if (stack.getItem() instanceof ItemBucket) {
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);
                } else {
                    Vec3i dirVec = EnumFacing.UP.getDirectionVec();

                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, stack, currentMlgBlock, EnumFacing.UP, new Vec3(dirVec.getX() * 0.5, dirVec.getY() * 0.5, dirVec.getZ() * 0.5).add(new Vec3(currentMlgBlock)))) {
                        mlgTimer.reset();
                    }
                }

                if (mc.thePlayer.inventory.currentItem != currentMlgItemIndex)
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
        }
    }
    private boolean inVoid() {
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

    private boolean inAir(int height, int plus) {
        if (mc.thePlayer.posY < 0)
            return false;
        for (int off = 0; off < height; off += plus) {
            AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, mc.thePlayer.posY - off, mc.thePlayer.posZ);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        jumped = true;
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}