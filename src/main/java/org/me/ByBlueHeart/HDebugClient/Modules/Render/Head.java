package org.me.ByBlueHeart.HDebugClient.Modules.Render;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.Render3DEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.render.RenderManagers;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "Head", description = "Allows you to see targets through walls.", category = ModuleCategory.RENDER)
public class Head extends Module {
    public final ListValue mode = new ListValue("Mode", new String[]{"Yaoer", "Taijun", "Ganga"},"Yaoer");

    private boolean isValid(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && entity.getHealth() >= 0.0f && entity != mc.thePlayer) {
            return true;
        }
        return false;
    }

    @EventTarget
    public void onRender(Render3DEvent event) {
        for (EntityPlayer entity : this.mc.theWorld.playerEntities) {
            if (!this.isValid((EntityLivingBase)entity)) continue;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.enableBlend();
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            float partialTicks = this.mc.timer.renderPartialTicks;
            this.mc.getRenderManager();
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManagers.renderPosX;
            this.mc.getRenderManager();
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManagers.renderPosY;
            this.mc.getRenderManager();
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManagers.renderPosZ;
            float DISTANCE = mc.thePlayer.getDistanceToEntity((Entity)entity);
            float DISTANCE_SCALE = Math.min((float)(DISTANCE * 0.15f), (float)0.15f);
            float SCALE = 0.035f;
            float xMid = (float)x;
            float yMid = (float)y + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f);
            float zMid = (float)z;
            GlStateManager.translate((float)((float)x), (float)((float)y + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f)), (float)((float)z));
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            this.mc.getRenderManager();
            GlStateManager.rotate((float)(- RenderManagers.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glScalef((- SCALE), (- SCALE), (float)(- (SCALE /= 2.0f)));
            Tessellator tesselator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tesselator.getWorldRenderer();
            Color gray = new Color(0, 0, 0);
            double thickness = 1.5f + DISTANCE * 0.01f;
            double xLeft = -20.0;
            double xRight = 20.0;
            double yUp = 27.0;
            double yDown = 130.0;
            double size = 10.0;
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GlStateManager.disableBlend();
            GL11.glDisable(3042);
            if (this.mode.get() == "Yaoer") {
                drawImage(new ResourceLocation("hdebug/Head/yaoer.png"), ((int)xLeft + 9), (int)((int)yUp - 20), (int)20, (int)25);
            } else if (this.mode.get() == "Ganga") {
                drawImage(new ResourceLocation("hdebug/Head/ganga.png"), ((int)xLeft + 7), (int)((int)yUp - 28), (int)27, (int)32);
            } else if (this.mode.get() == "Taijun") {
                drawImage(new ResourceLocation("hdebug/Head/taijun.png"), ((int)xLeft + 7), (int)((int)yUp - 28), (int)27, (int)32);
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glNormal3f(1.0f, 1.0f, (float)1.0f);
            GL11.glPopMatrix();
        }
    }
    public void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        boolean i = false;
        if (GL11.glIsEnabled(3042))
            i = true;
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        if (!i)
            GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
}
