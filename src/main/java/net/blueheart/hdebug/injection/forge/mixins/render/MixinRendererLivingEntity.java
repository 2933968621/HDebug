package net.blueheart.hdebug.injection.forge.mixins.render;

import co.uk.hexeption.utils.OutlineUtils;
import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.features.module.modules.render.TrueSight;
import net.blueheart.hdebug.utils.ChamsColor;
import net.blueheart.hdebug.utils.ClientUtils;
import net.blueheart.hdebug.utils.EntityUtils;
import net.blueheart.hdebug.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.me.ByBlueHeart.HDebugClient.Modules.Render.Chams;
import org.me.ByBlueHeart.HDebugClient.Modules.Render.ESP;
import org.me.ByBlueHeart.HDebugClient.Modules.Render.NameTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@SideOnly(Side.CLIENT)
@Mixin({RendererLivingEntity.class})
public abstract class MixinRendererLivingEntity
        extends MixinRender
{
    @Shadow
    protected ModelBase mainModel;

    @Inject(method = {"doRender"}, at = {@At("HEAD")})
    private <T extends net.minecraft.entity.EntityLivingBase> void injectChamsPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        Chams chams = (Chams) HDebug.moduleManager.getModule(Chams.class);

        if (chams.getState() && ((Boolean)chams.getTargetsValue().get()).booleanValue() && EntityUtils.isSelected((Entity)entity, false)) {
            switch ((String) chams.ModeValue().get()) {
                case "Color":
                    GL11.glEnable(32823);
                    GL11.glPolygonOffset(1.0F, -1000000.0F);
                    break;
                case "Textured":
                    GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                    GL11.glPolygonOffset(1.0F, -1000000F);
                    break;
            }
        }
    }

    @Inject(method = {"doRender"}, at = {@At("RETURN")})
    private <T extends net.minecraft.entity.EntityLivingBase> void injectChamsPost(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        Chams chams = (Chams) HDebug.moduleManager.getModule(Chams.class);

        if (chams.getState() && ((Boolean)chams.getTargetsValue().get()).booleanValue() && EntityUtils.isSelected((Entity)entity, false)) {
            switch ((String) chams.ModeValue().get()) {
                case "Color":
                    GL11.glPolygonOffset(1.0F, 1000000.0F);
                    GL11.glDisable(32823);
                    break;
                case "Textured":
                    GL11.glPolygonOffset(1.0F, 1000000F);
                    GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                    break;
            }
        }
    }

    @Inject(method = {"canRenderName"}, at = {@At("HEAD")}, cancellable = true)
    private <T extends net.minecraft.entity.EntityLivingBase> void canRenderName(T entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!ESP.renderNameTags || (HDebug.moduleManager.getModule(NameTags.class).getState() && EntityUtils.isSelected((Entity)entity, false))) {
            callbackInfoReturnable.setReturnValue(Boolean.valueOf(false));
        }
    }



    @Overwrite
    protected <T extends net.minecraft.entity.EntityLivingBase> void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
        boolean visible = !entitylivingbaseIn.isInvisible();
        TrueSight trueSight = (TrueSight) HDebug.moduleManager.getModule(TrueSight.class);
        boolean semiVisible = (!visible && (!entitylivingbaseIn.isInvisibleToPlayer((EntityPlayer)(Minecraft.getMinecraft()).thePlayer) || (trueSight.getState() && ((Boolean)trueSight.getEntitiesValue().get()).booleanValue())));
        Chams chams = (Chams) HDebug.moduleManager.getModule(Chams.class);
        if (visible || semiVisible) {
            if (!bindEntityTexture((Entity)entitylivingbaseIn)) {
                return;
            }
            if (semiVisible) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }

            ESP esp = (ESP) HDebug.moduleManager.getModule(ESP.class);
            if (esp.getState() && EntityUtils.isSelected(entitylivingbaseIn, false)) {
                Minecraft mc = Minecraft.getMinecraft();
                boolean fancyGraphics = mc.gameSettings.fancyGraphics;
                mc.gameSettings.fancyGraphics = false;
                float gamma = mc.gameSettings.gammaSetting;
                mc.gameSettings.gammaSetting = 100000.0F;
                switch(esp.modeValue.get().toLowerCase()) {
                    case "wireframe":
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        GL11.glEnable(GL11.GL_LINE_SMOOTH);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        RenderUtils.glColor(esp.getColor(entitylivingbaseIn));
                        GL11.glLineWidth(esp.wireframeWidth.get());
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    case "outline":
                        ClientUtils.disableFastRender();
                        GlStateManager.resetColor();

                        final Color color = esp.getColor(entitylivingbaseIn);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderOne(esp.outlineWidth.get());
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderTwo();
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderThree();
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFour(color);
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                }
                mc.gameSettings.fancyGraphics = fancyGraphics;
                mc.gameSettings.gammaSetting = gamma;
            }
            if (chams.getState()) {
                if (chams.getState() && chams.getTargetsValue().get()) {
                    switch ((String) chams.ModeValue().get()) {
                        case "Color":
                            GL11.glPushMatrix();
                            GL11.glPushAttrib(1048575);
                            GL11.glDisable(2929);
                            GL11.glDisable(3553);
                            GL11.glEnable(3042);
                            GL11.glBlendFunc(770, 771);
                            GL11.glDisable(2896);
                            GL11.glPolygonMode(1032, 6914);

                            GL11.glColor4d(!(Minecraft.getMinecraft()).thePlayer.canEntityBeSeen((Entity) entitylivingbaseIn) ? 1.0D : ChamsColor.red, !(Minecraft.getMinecraft()).thePlayer.canEntityBeSeen((Entity) entitylivingbaseIn) ? 0.5D : ChamsColor.green, !(Minecraft.getMinecraft()).thePlayer.canEntityBeSeen((Entity) entitylivingbaseIn) ? 0.0D : ChamsColor.blue, ChamsColor.Apl);
                            this.mainModel.render((Entity) entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                            GL11.glEnable(2896);
                            GL11.glDisable(3042);
                            GL11.glEnable(3553);
                            GL11.glEnable(2929);
                            GL11.glColor3d(1.0D, 1.0D, 1.0D);
                            GL11.glPopAttrib();
                            GL11.glPopMatrix();
                            break;
                        case "Textured":
                            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                            break;
                    }
                } else {
                    this.mainModel.render((Entity) entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                }
            } else {
                this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            }
            if (semiVisible) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }
}
