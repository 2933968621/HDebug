package net.blueheart.hdebug.injection.forge.mixins.item;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.features.module.modules.render.AntiBlind;
import net.blueheart.hdebug.features.module.modules.render.SwingAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.Aura;
import org.me.ByBlueHeart.HDebugClient.Modules.Render.Animation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@SideOnly(Side.CLIENT)
public abstract class MixinItemRenderer {

    @Shadow
    private float prevEquippedProgress;

    @Shadow
    private float equippedProgress;

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    protected abstract void rotateArroundXAndY(float angle, float angleY);

    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);

    @Shadow
    protected abstract void transformFirstPersonItem(float equipProgress, float swingProgress);

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks);

    @Shadow
    protected abstract void doBlockTransformations();

    @Shadow
    protected abstract void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);

    @Shadow
    protected abstract void doItemUsedTransformations(float swingProgress);

    @Shadow
    public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);

    @Shadow
    protected abstract void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress);

    /**
     * @author
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        float var22 = mc.thePlayer.getSwingProgress(partialTicks);
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP) abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if(this.itemToRender != null) {
            final Aura killAura = (Aura) HDebug.moduleManager.getModule(Aura.class);

            if(this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap) {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.getItemInUseCount() > 0 || (itemToRender.getItem() instanceof ItemSword && killAura.getBlockingStatus())) {
                EnumAction enumaction = killAura.getBlockingStatus() ? EnumAction.BLOCK : this.itemToRender.getItemUseAction();

                switch (enumaction) {
                    case NONE:
                        transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, f1);
                        break;
                    case BLOCK:
                        if (HDebug.moduleManager.getModule(Animation.class).getState()) {
                            switch (Animation.AnimationModeValue.get()) {
                                case "Slide":
                                    this.tap(f, f1);
                                    doBlockTransformations();
                                    break;
                                case "Sigma":
                                    transformFirstPersonItem(f, 0.0F);
                                    doBlockTransformations();
                                    float f4 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.translate(-0.0F, 0.4F, 1.0F);
                                    GlStateManager.rotate(-f4 * 22.5F, -9.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-f4 * 10.0F, 1.0F, -0.4F, -0.5F);
                                    break;
                                case "NoSwing":
                                    transformFirstPersonItem(f, 0.0F);
                                    doBlockTransformations();
                                    break;
                                case "HDebug":
                                    transformFirstPersonItem(f, 0.0F);
                                    doBlockTransformations();
                                    float var15 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.scale(0.54F, 0.54F, 0.54F);
                                    GlStateManager.translate(-0.4F, 1F, 0.4F);
                                    GlStateManager.rotate(-var15 * 90.0F, -15.0F, -15.0F, 19.0F);
                                    break;
                                case "Push":
                                    this.transformFirstPersonItem(f, 0.0F);
                                    this.doBlockTransformations();
                                    var15 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.translate(-0.0F, 0.4F, 0.3F);
                                    GlStateManager.rotate(-var15 * 35.0F, -8.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-var15 * 10.0F, 1.0F, -0.4F, -0.5F);
                                case "ExhibitionSwang":
                                    transformFirstPersonItem(f / 2.0F, f1);
                                    float Swang = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.rotate(Swang * 30.0F / 2.0F, -Swang, -0.0F, 9.0F);
                                    GlStateManager.rotate(Swang * 40.0F, 1.0F, -Swang / 2.0F, -0.0F);
                                    doBlockTransformations();
                                    break;
                                case "ExhibitionSwank":
                                    transformFirstPersonItem(f / 2.0F, f1);
                                    float Swank = MathHelper.sin(MathHelper.sqrt_float(f) * 3.1415927F);
                                    GlStateManager.rotate(Swank * 30.0F, -Swank, -0.0F, 9.0F);
                                    GlStateManager.rotate(Swank * 40.0F, 1.0F, -Swank, -0.0F);
                                    doBlockTransformations();
                                    break;
                                case "ExhibitionSwong":
                                    transformFirstPersonItem(f / 2.0F, 0.0F);
                                    float Swong = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.rotate(-Swong * 40.0F / 2.0F, Swong / 2.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-Swong * 30.0F, 1.0F, Swong / 2.0F, -0.0F);
                                    doBlockTransformations();
                                    break;
                                case "Jigsaw":
                                    transformFirstPersonItem(0.1F, f1);
                                    doBlockTransformations();
                                    GlStateManager.translate(-0.5D, 0.0D, 0.0D);
                                    break;
                                case "1.7":
                                    genCustom(f, f1);
                                    doBlockTransformations();
                                    break;
                                case "Luna":
                                    transformFirstPersonItem(f, 0.0F);
                                    doBlockTransformations();
                                    float sin = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.scale(1.0F, 1.0F, 1.0F);
                                    GlStateManager.translate(-0.2F, 0.45F, 0.25F);
                                    GlStateManager.rotate(-sin * 20.0F, -5.0F, -5.0F, 9.0F);
                                    break;
                                case "Jello":
                                    transformFirstPersonItem(0.0F, 0.0F);
                                    doBlockTransformations();
                                    int alpha = (int)Math.min(255L, ((System.currentTimeMillis() % 255L > 127L) ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : (System.currentTimeMillis() % 255L)) * 2L);
                                    GlStateManager.translate(0.3F, -0.0F, 0.4F);
                                    GlStateManager.rotate(0.0F, 0.0F, 0.0F, 1.0F);
                                    GlStateManager.translate(0.0F, 0.5F, 0.0F);
                                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, -1.0F);
                                    GlStateManager.translate(0.6F, 0.5F, 0.0F);
                                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, -1.0F);
                                    GlStateManager.rotate(-10.0F, 1.0F, 0.0F, -1.0F);
                                    GlStateManager.rotate(mc.thePlayer.isSwingInProgress ? (-alpha / 5.0F) : 1.0F, 1.0F, -0.0F, 1.0F);
                                    break;
                                case "360°":
                                    genCustom(0.0F, 0.0F);
                                    doBlockTransformations();
                                    GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                                    GlStateManager.rotate(MathHelper.sqrt_float(f1) * 10.0F * 40.0F, 1.0F, -0.0F, 2.0F);
                                    break;
                                case "ETB":
                                    this.ETB(f, f1);
                                    this.doBlockTransformations();
                                    break;
                                case "Avatar":
                                    this.avatar(f, f1);
                                    this.doBlockTransformations();
                                    break;
                                case "IDBUG":
                                    this.IDBUG(f, f1);
                                    this.doBlockTransformations();
                                    break;
                                case "NoSword":
                                    this.transformFirstPersonItem(f2, 0.0F);
                                    this.doBlockTransformations();
                                    float var9 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.rotate(-var9 * 70.0F / 2.0F, -2.0F, -0.0F, 2.0F);
                                    GlStateManager.rotate(-var9 * 70.0F, 1.0F, -0.4F, -0.0F);
                                    break;
                                case "Normal":
                                    this.genCustom(0.0F, 0.0F);
                                    this.doBlockTransformations();
                                    float var1 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.translate(-0.5F, 0.4F, 0.0F);
                                    GlStateManager.rotate(-var1 * 50.0F, -8.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-var1 * 70.0F, 1.0F, -0.4F, -0.0F);
                                    break;
                                case "Custom":
                                    this.genCustom(Animation.CustomValue2.get(), Animation.CustomValue.get());
                                    this.doBlockTransformations();
                                    float var0 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                                    GlStateManager.translate(-0.5F, 0.4F, 0.0F);
                                    GlStateManager.rotate(-var0 * 50.0F, -8.0F, -0.0F, 9.0F);
                                    GlStateManager.rotate(-var0 * 70.0F, 1.0F, -0.4F, -0.0F);
                                    break;
                                case "Rotate":
                                    Random(var22);
                                    this.doBlockTransformations();
                                    break;
                            }
                            break;
                        }
                        transformFirstPersonItem(f + 0.1F, f1);
                        doBlockTransformations();
                        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                        break;
                    case BOW:
                        this.transformFirstPersonItem(f, f1);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                        break;
                }
            }else{
                if (!HDebug.moduleManager.getModule(SwingAnimation.class).getState())
                    this.doItemUsedTransformations(f1);
                this.transformFirstPersonItem(f, f1);
            }
            if (HDebug.moduleManager.getModule(Animation.class).getState() && Animation.EveryThingBlock.get() == true && mc.gameSettings.keyBindUseItem.isKeyDown()){
                GL11.glTranslated(Animation.X.get(), Animation.Y.get(), Animation.Z.get());
                doBlockTransformations();
            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }else if(!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    int ticks = 0;
    private void tap(float var2, float swingProgress) {
        float smooth = (swingProgress*0.8f - (swingProgress*swingProgress)*0.8f);
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F,  var2 * -0.15F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        GlStateManager.rotate(smooth * -90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.37F, 0.37F, 0.37F);
    }
    private void Random(float swingProgress) {
        ticks += 1;
        GlStateManager.translate(0.7, -0.4F, -0.8F);
        GlStateManager.rotate(50, 1, 0, 0);
        GlStateManager.rotate(50, 0, 0, -1);
        GlStateManager.rotate((ticks) * 0.2f * Animation.Speed.get(), 0, 0, 1);
        GlStateManager.rotate(-25f, 1, 0, 0);
        GlStateManager.scale(0.40, 0.40, 0.40);
    }
    private void IDBUG(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.57f, -0.53f, -0.71999997f);
        GlStateManager.translate(0.1f, p_178096_1_ * -0.8f, 0.1f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927f);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927f);
        GlStateManager.rotate(var3 * -21.0f, 0.0f, 1.0f, 0.2f);
        GlStateManager.rotate(var4 * -10.7f, 0.2f, 0.1f, 1.0f);
        GlStateManager.rotate(var4 * -50.6f, 1.3f, 1.1f, 0.2f);
        GlStateManager.scale(0.3f, 0.4f, 0.3f);
    }

    private void ETB(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, p_178096_1_ * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927f);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927f);
        GlStateManager.rotate(var3 * -34.0f, 0.0f, 1.0f, 0.2f);
        GlStateManager.rotate(var4 * -20.7f, 0.2f, 0.1f, 1.0f);
        GlStateManager.rotate(var4 * -68.6f, 1.3f, 0.1f, 0.2f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private void avatar(float equipProgress, float swingProgress){
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, 0, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -40.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }

    private void genCustom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927F);
        GlStateManager.rotate(var3 * -34.0F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -20.7F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -68.6F, 1.3F, 0.1F, 0.2F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void renderFireInFirstPerson(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind) HDebug.moduleManager.getModule(AntiBlind.class);

        if(antiBlind.getState() && antiBlind.getFireEffect().get()) callbackInfo.cancel();
    }
}