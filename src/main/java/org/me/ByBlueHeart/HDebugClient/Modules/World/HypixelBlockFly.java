package org.me.ByBlueHeart.HDebugClient.Modules.World;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.*;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.features.module.modules.movement.Speed;
import net.blueheart.hdebug.ui.font.FontManager;
import net.blueheart.hdebug.ui.font.UnicodeFontRenderer;
import net.blueheart.hdebug.utils.InventoryUtils;
import net.blueheart.hdebug.utils.PlaceRotation;
import net.blueheart.hdebug.utils.Rotation;
import net.blueheart.hdebug.utils.RotationUtils;
import net.blueheart.hdebug.utils.block.BlockUtils;
import net.blueheart.hdebug.utils.block.PlaceInfo;
import net.blueheart.hdebug.utils.render.RenderUtils;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.blueheart.hdebug.utils.timer.TimeUtils;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.IntegerValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils;

import java.awt.*;

;

@ModuleInfo(name = "HypixelBlockFly", description = "Automatically places blocks beneath your feet.", category = ModuleCategory.WORLD)
public class HypixelBlockFly extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[] { "Normal", "Rewinside", "Expand" }, "Normal");

    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 0, 0, 1000) {
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = ((Integer)HypixelBlockFly.this.minDelayValue.get()).intValue();
            if (i > newValue.intValue())
                set(Integer.valueOf(i));
        }
    };

    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 0, 0, 1000) {
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = ((Integer)HypixelBlockFly.this.maxDelayValue.get()).intValue();
            if (i < newValue.intValue())
                set(Integer.valueOf(i));
        }
    };

    private final BoolValue placeableDelay = new BoolValue("PlaceableDelay", false);

    private final BoolValue autoBlockValue = new BoolValue("AutoBlock", true);

    private final BoolValue stayAutoBlock = new BoolValue("StayAutoBlock", false);

    public final BoolValue sprintValue = new BoolValue("Sprint", true);

    private final BoolValue swingValue = new BoolValue("Swing", true);

    private final BoolValue downValue = new BoolValue("Down", false);

    private final BoolValue searchValue = new BoolValue("Search", true);

    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[] { "Pre", "Post" }, "Post");

    private final BoolValue eagleValue = new BoolValue("Eagle", false);

    private final BoolValue eagleSilentValue = new BoolValue("EagleSilent", false);

    private final IntegerValue blocksToEagleValue = new IntegerValue("BlocksToEagle", 0, 0, 10);

    private final FloatValue eagleEdgeDistanceValue = new FloatValue("EagleEdgeDistance", 0.2F, 0.0F, 0.5F);

    private final IntegerValue expandLengthValue = new IntegerValue("ExpandLength", 5, 1, 6);

    public final ListValue rotationModeValue = new ListValue("RotationMode", new String[] { "Normal", "Static", "StaticPitch", "StaticYaw" }, "Normal");

    private final BoolValue rotationsValue = new BoolValue("Rotations", true);

    private final FloatValue staticPitchValue = new FloatValue("StaticPitch", 86.0F, 70.0F, 90.0F);

    private final IntegerValue keepLengthValue = new IntegerValue("KeepRotationLength", 0, 0, 20);

    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false) {
        protected void onChanged(Boolean oldValue, Boolean newValue) {
            if (!newValue.booleanValue())
                HypixelBlockFly.this.rotationStrafeValue.set(Boolean.valueOf(false));
        }
    };

    private final BoolValue rotationStrafeValue = new BoolValue("RotationStrafe", false) {
        protected void onChanged(Boolean oldValue, Boolean newValue) {
            if (newValue.booleanValue())
                HypixelBlockFly.this.keepRotationValue.set(Boolean.valueOf(true));
        }
    };

    private final BoolValue zitterValue = new BoolValue("Zitter", false);

    private final ListValue zitterModeValue = new ListValue("ZitterMode", new String[] { "Teleport", "Smooth" }, "Teleport");

    private final FloatValue zitterSpeed = new FloatValue("ZitterSpeed", 0.13F, 0.1F, 0.3F);

    private final FloatValue zitterStrength = new FloatValue("ZitterStrength", 0.072F, 0.05F, 0.2F);

    private final FloatValue timerValue = new FloatValue("Timer", 1.0F, 0.1F, 10.0F);

    private final FloatValue speedModifierValue = new FloatValue("SpeedModifier", 1.0F, 0.0F, 2.0F);

    private final BoolValue sameYValue = new BoolValue("SameY", false);

    private final BoolValue safeWalkValue = new BoolValue("SafeWalk", true);

    private final BoolValue airSafeValue = new BoolValue("AirSafe", false);

    private final BoolValue counterDisplayValue = new BoolValue("Counter", true);

    private final BoolValue tower = new BoolValue("Tower", true);

    public static final BoolValue LagValue = new BoolValue("StopSpeed", true);

    private final BoolValue markValue = new BoolValue("Mark", false);

    private PlaceInfo targetPlace;

    private int launchY;

    private Rotation lockRotation;

    private int slot;

    private boolean zitterDirection;

    private final MSTimer delayTimer = new MSTimer();

    private final MSTimer zitterTimer = new MSTimer();

    private long delay;

    private int placedBlocksWithoutEagle = 0;

    private boolean eagleSneaking;

    private boolean shouldGoDown = false;

    public void onEnable() {
        if (mc.thePlayer == null)
            return;
        this.launchY = (int)mc.thePlayer.posY;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc.timer.timerSpeed = ((Float)this.timerValue.get()).floatValue();
        this.shouldGoDown = (((Boolean)this.downValue.get()).booleanValue() && GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && getBlocksAmount() > 1);
        if (this.shouldGoDown)
            mc.gameSettings.keyBindSneak.pressed = false;
        if (mc.thePlayer.onGround) {
            String mode = (String)this.modeValue.get();
            if (mode.equalsIgnoreCase("Rewinside")) {
                MovementUtils.strafe(0.2F);
                mc.thePlayer.motionY = 0.0D;
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("smooth")) {
                if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight))
                    mc.gameSettings.keyBindRight.pressed = false;
                if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft))
                    mc.gameSettings.keyBindLeft.pressed = false;
                if (this.zitterTimer.hasTimePassed(100L)) {
                    this.zitterDirection = !this.zitterDirection;
                    this.zitterTimer.reset();
                }
                if (this.zitterDirection) {
                    mc.gameSettings.keyBindRight.pressed = true;
                    mc.gameSettings.keyBindLeft.pressed = false;
                } else {
                    mc.gameSettings.keyBindRight.pressed = false;
                    mc.gameSettings.keyBindLeft.pressed = true;
                }
            }
            if (((Boolean)this.eagleValue.get()).booleanValue() && !this.shouldGoDown) {
                double dif = 0.5D;
                if (((Float)this.eagleEdgeDistanceValue.get()).floatValue() > 0.0F)
                    for (int i = 0; i < 4; i++) {
                        BlockPos blockPos = new BlockPos(mc.thePlayer.posX + ((i == 0) ? -1 : ((i == 1) ? 1 : 0)), mc.thePlayer.posY - ((mc.thePlayer.posY == (int)mc.thePlayer.posY + 0.5D) ? 0.0D : 1.0D), mc.thePlayer.posZ + ((i == 2) ? -1 : ((i == 3) ? 1 : 0)));
                        PlaceInfo placeInfo = PlaceInfo.get(blockPos);
                        if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                            double calcDif = (i > 1) ? (mc.thePlayer.posZ - blockPos.getZ()) : (mc.thePlayer.posX - blockPos.getX());
                            calcDif -= 0.5D;
                            if (calcDif < 0.0D)
                                calcDif *= -1.0D;
                            calcDif -= 0.5D;
                            if (calcDif < dif)
                                dif = calcDif;
                        }
                    }
                if (this.placedBlocksWithoutEagle >= ((Integer)this.blocksToEagleValue.get()).intValue()) {
                    boolean shouldEagle = (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ)).getBlock() == Blocks.air || dif < ((Float)this.eagleEdgeDistanceValue.get()).floatValue());
                    if (((Boolean)this.eagleSilentValue.get()).booleanValue()) {
                        if (this.eagleSneaking != shouldEagle)
                            mc.getNetHandler().addToSendQueue((net.minecraft.network.Packet) new C0BPacketEntityAction(mc.thePlayer, shouldEagle ? C0BPacketEntityAction.Action.START_SNEAKING : C0BPacketEntityAction.Action.STOP_SNEAKING));
                        this.eagleSneaking = shouldEagle;
                    } else {
                        mc.gameSettings.keyBindSneak.pressed = shouldEagle;
                    }
                    this.placedBlocksWithoutEagle = 0;
                } else {
                    this.placedBlocksWithoutEagle++;
                }
            }
            if (((Boolean)this.zitterValue.get()).booleanValue() && ((String)this.zitterModeValue.get()).equalsIgnoreCase("teleport")) {
                MovementUtils.strafe(((Float)this.zitterSpeed.get()).floatValue());
                double yaw = Math.toRadians(mc.thePlayer.rotationYaw + (this.zitterDirection ? 90.0D : -90.0D));
                mc.thePlayer.motionX -= Math.sin(yaw) * ((Float)this.zitterStrength.get()).floatValue();
                mc.thePlayer.motionZ += Math.cos(yaw) * ((Float)this.zitterStrength.get()).floatValue();
                this.zitterDirection = !this.zitterDirection;
            }
        }
        if (tower.get() && Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !HDebug.moduleManager.getModule(Speed.class).getState()) {
            HDebug.moduleManager.getModule(Tower.class).setState(true);
        } else {
            HDebug.moduleManager.getModule(Tower.class).setState(false);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (mc.thePlayer == null)
            return;
        net.minecraft.network.Packet<?> packet = event.getPacket();
        if (packet instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange)packet;
            this.slot = packetHeldItemChange.getSlotId();
        }
    }

    @EventTarget
    private void onStrafe(StrafeEvent event) {
        if (!((Boolean)this.rotationStrafeValue.get()).booleanValue())
            return;
        if (this.lockRotation != null && ((Boolean)this.keepRotationValue.get()).booleanValue()) {
            int dif = (int)((MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw - this.lockRotation.getYaw() - 23.5F - 135.0F) + 180.0F) / 45.0F);
            float yaw = this.lockRotation.getYaw();
            float strafe = event.getStrafe();
            float forward = event.getForward();
            float friction = event.getFriction();
            float calcForward = 0.0F;
            float calcStrafe = 0.0F;
            switch (dif) {
                case 0:
                    calcForward = forward;
                    calcStrafe = strafe;
                    break;
                case 1:
                    calcForward += forward;
                    calcStrafe -= forward;
                    calcForward += strafe;
                    calcStrafe += strafe;
                    break;
                case 2:
                    calcForward = strafe;
                    calcStrafe = -forward;
                    break;
                case 3:
                    calcForward -= forward;
                    calcStrafe -= forward;
                    calcForward += strafe;
                    calcStrafe -= strafe;
                    break;
                case 4:
                    calcForward = -forward;
                    calcStrafe = -strafe;
                    break;
                case 5:
                    calcForward -= forward;
                    calcStrafe += forward;
                    calcForward -= strafe;
                    calcStrafe -= strafe;
                    break;
                case 6:
                    calcForward = -strafe;
                    calcStrafe = forward;
                    break;
                case 7:
                    calcForward += forward;
                    calcStrafe += forward;
                    calcForward -= strafe;
                    calcStrafe += strafe;
                    break;
            }
            if (calcForward > 1.0F) {
                calcForward *= 0.5F;
            } else if (calcForward < 0.9F && calcForward > 0.3F) {
                calcForward *= 0.5F;
            }
            if (calcForward < -1.0F) {
                calcForward *= 0.5F;
            } else if (calcForward > -0.9F && calcForward < -0.3F) {
                calcForward *= 0.5F;
            }
            if (calcStrafe > 1.0F) {
                calcStrafe *= 0.5F;
            } else if (calcStrafe < 0.9F && calcStrafe > 0.3F) {
                calcStrafe *= 0.5F;
            }
            if (calcStrafe < -1.0F) {
                calcStrafe *= 0.5F;
            } else if (calcStrafe > -0.9F && calcStrafe < -0.3F) {
                calcStrafe *= 0.5F;
            }
            float f = calcStrafe * calcStrafe + calcForward * calcForward;
            if (f >= 1.0E-4F) {
                f = MathHelper.sqrt_float(f);
                if (f < 1.0F)
                    f = 1.0F;
                f = friction / f;
                calcStrafe *= f;
                calcForward *= f;
                float yawSin = MathHelper.sin((float)(yaw * Math.PI / 180.0D));
                float yawCos = MathHelper.cos((float)(yaw * Math.PI / 180.0D));
                mc.thePlayer.motionX += (calcStrafe * yawCos - calcForward * yawSin);
                mc.thePlayer.motionZ += (calcForward * yawCos + calcStrafe * yawSin);
            }
            event.cancelEvent();
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null)
            RotationUtils.setTargetRotation(this.lockRotation);
        String mode = (String)this.modeValue.get();
        EventState eventState = event.getEventState();
        if (((String)this.placeModeValue.get()).equalsIgnoreCase(eventState.getStateName()))
            place();
        if (eventState == EventState.PRE) {
            if (((Boolean)this.autoBlockValue.get()).booleanValue() ? (InventoryUtils.findAutoBlockBlock() == -1) : (mc.thePlayer.getHeldItem() == null ||
                    !(mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemBlock)))
                return;
            findBlock(mode.equalsIgnoreCase("expand"));
        }
        if (this.targetPlace == null && (
                (Boolean)this.placeableDelay.get()).booleanValue())
            this.delayTimer.reset();
    }

    private void findBlock(boolean expand) {
        BlockPos blockPosition = this.shouldGoDown ? ((mc.thePlayer.posY == (int)mc.thePlayer.posY + 0.5D) ? new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.6D, mc.thePlayer.posZ) : (new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.6D, mc.thePlayer.posZ)).down()) : ((mc.thePlayer.posY == (int)mc.thePlayer.posY + 0.5D) ? new BlockPos((Entity)mc.thePlayer) : (new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).down());
        if (!expand && (!BlockUtils.isReplaceable(blockPosition) || search(blockPosition, !this.shouldGoDown)))
            return;
        if (expand) {
            for (int i = 0; i < ((Integer)this.expandLengthValue.get()).intValue(); i++) {
                if (search(blockPosition.add(
                        (mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST) ? -i : ((mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST) ? i : 0), 0,

                        (mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH) ? -i : ((mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH) ? i : 0)), false))
                    return;
            }
        } else if (((Boolean)this.searchValue.get()).booleanValue()) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (search(blockPosition.add(x, 0, z), !this.shouldGoDown))
                        return;
                }
            }
        }
    }

    private void place() {
        if (this.targetPlace == null) {
            if (((Boolean)this.placeableDelay.get()).booleanValue())
                this.delayTimer.reset();
            return;
        }
        if (!this.delayTimer.hasTimePassed(this.delay) || (((Boolean)this.sameYValue.get()).booleanValue() && this.launchY - 1 != (int)(this.targetPlace.getVec3()).yCoord))
            return;
        int blockSlot = -1;
        ItemStack itemStack = mc.thePlayer.getHeldItem();
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemBlock)) {
            if (!((Boolean)this.autoBlockValue.get()).booleanValue())
                return;
            blockSlot = InventoryUtils.findAutoBlockBlock();
            if (blockSlot == -1)
                return;
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
            itemStack = mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
        }
        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, this.targetPlace.getBlockPos(), this.targetPlace
                .getEnumFacing(), this.targetPlace.getVec3())) {
            this.delayTimer.reset();
            this.delay = TimeUtils.randomDelay(((Integer)this.minDelayValue.get()).intValue(), ((Integer)this.maxDelayValue.get()).intValue());
            if (mc.thePlayer.onGround) {
                float modifier = ((Float)this.speedModifierValue.get()).floatValue();
                mc.thePlayer.motionX *= modifier;
                mc.thePlayer.motionZ *= modifier;
            }
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                mc.thePlayer.swingItem();
            } else {
                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            }
        }
        if (!((Boolean)this.stayAutoBlock.get()).booleanValue() && blockSlot >= 0)
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        this.targetPlace = null;
    }

    public void onDisable() {
        HDebug.moduleManager.getModule(Tower.class).setState(false);
        if (mc.thePlayer == null)
            return;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
            mc.gameSettings.keyBindSneak.pressed = false;
            if (this.eagleSneaking)
                mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight))
            mc.gameSettings.keyBindRight.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft))
            mc.gameSettings.keyBindLeft.pressed = false;
        this.lockRotation = null;
        mc.timer.timerSpeed = 1.0F;
        this.shouldGoDown = false;
        if (this.slot != mc.thePlayer.inventory.currentItem)
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (!((Boolean)this.safeWalkValue.get()).booleanValue() || this.shouldGoDown)
            return;
        if (((Boolean)this.airSafeValue.get()).booleanValue() || mc.thePlayer.onGround)
            event.setSafeWalk(true);
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (counterDisplayValue.get()) {
            /**
             GlStateManager.pushMatrix();

             final BlockOverlay blockOverlay = (BlockOverlay) HDebug.moduleManager.getModule(BlockOverlay.class);
             if (blockOverlay.getState() && blockOverlay.getInfoValue().get() && blockOverlay.getCurrentBlock() != null)
             GlStateManager.translate(0, 15F, 0);

             final String info = getBlocksAmount() + "§7 Blocks";
             final ScaledResolution scaledResolution = new ScaledResolution(mc);
             RenderUtils.drawBorderedRect((scaledResolution.getScaledWidth() / 2) - 2, (scaledResolution.getScaledHeight() / 2) + 5, (scaledResolution.getScaledWidth() / 2) + Fonts.font40.getStringWidth(info) + 2, (scaledResolution.getScaledHeight() / 2) + 16, 3, Color.BLACK.getRGB(), Color.BLACK.getRGB());
             GlStateManager.resetColor();
             Fonts.font40.drawString(info, scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 2 + 7, Color.WHITE.getRGB());

             GlStateManager.popMatrix();
             */
            ScaledResolution res = new ScaledResolution(this.mc);
            UnicodeFontRenderer font = FontManager.yahei20;
            if (getBlocksAmount() != -1)
                if (mc.thePlayer != null && mc.thePlayer.inventory.mainInventory[slot] != null) {
                    if (mc.thePlayer.inventory.mainInventory[slot].getItem() instanceof ItemBlock) {
                        String Text = getBlocksAmount() + " Blocks";
                        int AddX = font.getStringWidth(Text) + 4;
                        // Rect
                        RenderUtils.drawFastRoundedRect(res.getScaledWidth() / 2 - 12 - 4 - AddX / 2, 4,
                                res.getScaledWidth() / 2 + 12f + 4 + AddX / 2, 4 + 24 + 6, 2,
                                new Color(0, 0, 0, 160).getRGB());

                        // ICON
//					RenderUtil.drawImage(new ResourceLocation("Client/Block.png"),
//							res.getScaledWidth() / 2 - 12 - AddX / 2, 8, 24, 24);
                        GL11.glPushMatrix();
                        RenderItem ir = new RenderItem(mc.getTextureManager(), new ModelManager(new TextureMap("textures", true)));
                        GlStateManager.scale(1.6f, 1.6f, 1.6f);
                        RenderHelper.enableGUIStandardItemLighting();
                        ir.renderItemIntoGUI(mc.thePlayer.inventory.mainInventory[slot],
                                (int) ((res.getScaledWidth() / 2 - 12 - AddX / 2) / 1.6f), (int) ((7) / 1.6f));
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.enableAlpha();
                        GlStateManager.disableCull();
                        GlStateManager.disableBlend();
                        GlStateManager.disableLighting();
                        GlStateManager.clear(256);
                        GL11.glPopMatrix();
                        // Text
                        font.drawStringWithShadow(Text, res.getScaledWidth() / 2 - 12 - 4 - AddX / 2 + 6 + 24, 4 + 10,
                                Color.WHITE.getRGB());
                    }

                }
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (!((Boolean)this.markValue.get()).booleanValue())
            return;
        for (int i = 0; i < (((String)this.modeValue.get()).equalsIgnoreCase("Expand") ? (((Integer)this.expandLengthValue.get()).intValue() + 1) : 2); i++) {
            final BlockPos blockPos = new BlockPos(mc.thePlayer.posX + (mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST ? i : 0), mc.thePlayer.posY - (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? 0D : 1.0D) - (shouldGoDown ? 1D : 0), mc.thePlayer.posZ + (mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH ? i : 0));
            PlaceInfo placeInfo = PlaceInfo.get(blockPos);
            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                RenderUtils.drawBlockBox(blockPos, new Color(0, 255, 247, 100), false);
                break;
            }
        }
    }

    private boolean search(BlockPos blockPosition, boolean checks) {
        if (!BlockUtils.isReplaceable(blockPosition))
            return false;
        boolean staticMode = ((String)this.rotationModeValue.get()).equalsIgnoreCase("Static");
        boolean staticPitchMode = (staticMode || ((String)this.rotationModeValue.get()).equalsIgnoreCase("StaticPitch"));
        boolean staticYawMode = (staticMode || ((String)this.rotationModeValue.get()).equalsIgnoreCase("StaticYaw"));
        float staticPitch = ((Float)this.staticPitchValue.get()).floatValue();
        Vec3 eyesPos = new Vec3(mc.thePlayer.posX, (mc.thePlayer.getEntityBoundingBox()).minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        PlaceRotation placeRotation = null;
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = blockPosition.offset(side);
            if (BlockUtils.canBeClicked(neighbor)) {
                Vec3 dirVec = new Vec3(side.getDirectionVec());
                double xSearch;
                for (xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
                    double ySearch;
                    for (ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
                        double zSearch;
                        for (zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
                            Vec3 posVec = (new Vec3((Vec3i)blockPosition)).addVector(xSearch, ySearch, zSearch);
                            double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                            Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5D, dirVec.yCoord * 0.5D, dirVec.zCoord * 0.5D));
                            if (!checks || (eyesPos.squareDistanceTo(hitVec) <= 18.0D && distanceSqPosVec <= eyesPos.squareDistanceTo(posVec.add(dirVec)) && mc.theWorld.rayTraceBlocks (eyesPos, hitVec, false, true, false) == null))
                                for (int i = 0; i < (staticYawMode ? 2 : 1); i++) {
                                    double diffX = (staticYawMode && i == 0) ? 0.0D : (hitVec.xCoord - eyesPos.xCoord);
                                    double diffY = hitVec.yCoord - eyesPos.yCoord;
                                    double diffZ = (staticYawMode && i == 1) ? 0.0D : (hitVec.zCoord - eyesPos.zCoord);
                                    double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
                                    Rotation rotation = new Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F), staticPitchMode ? staticPitch : MathHelper.wrapAngleTo180_float((float)-Math.toDegrees(Math.atan2(diffY, diffXZ))));
                                    Vec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                                    Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4.0D, rotationVector.yCoord * 4.0D, rotationVector.zCoord * 4.0D);
                                    MovingObjectPosition obj = mc.theWorld.rayTraceBlocks (eyesPos, vector, false, false, true);
                                    if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && obj.getBlockPos().equals(neighbor))
                                        if (placeRotation == null || RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation()))
                                            placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                                }
                        }
                    }
                }
            }
        }
        if (placeRotation == null)
            return false;
        if (((Boolean)this.rotationsValue.get()).booleanValue()) {
            RotationUtils.setTargetRotation(placeRotation.getRotation(), ((Integer)this.keepLengthValue.get()).intValue());
            this.lockRotation = placeRotation.getRotation();
        }
        this.targetPlace = placeRotation.getPlaceInfo();
        return true;
    }

    private int getBlocksAmount() {
        int amount = 0;
        for (int i = 36; i < 45; i++) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof net.minecraft.item.ItemBlock)
                amount += itemStack.stackSize;
        }
        return amount;
    }

    public String getTag() {
        return (String)this.modeValue.get();
    }
}