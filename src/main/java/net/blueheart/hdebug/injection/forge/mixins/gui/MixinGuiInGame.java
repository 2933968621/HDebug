package net.blueheart.hdebug.injection.forge.mixins.gui;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.Render2DEvent;
import net.blueheart.hdebug.features.module.modules.render.AntiBlind;
import org.me.ByBlueHeart.HDebugClient.Modules.Render.HUD;
import net.blueheart.hdebug.features.module.modules.render.NoScoreboard;
import net.blueheart.hdebug.ui.font.FontManager;
import net.blueheart.hdebug.ui.font.Fonts;
import net.blueheart.hdebug.utils.ClassUtils;
import net.blueheart.hdebug.utils.EntityUtils;
import net.blueheart.hdebug.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mixin(GuiIngame.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiInGame {

    @Shadow
    protected abstract void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player);

    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    private void renderScoreboard(CallbackInfo callbackInfo) {
        if (HDebug.moduleManager.getModule(HUD.class).getState() || NoScoreboard.INSTANCE.getState())
            callbackInfo.cancel();
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        final HUD hud = (HUD) HDebug.moduleManager.getModule(HUD.class);

        if(Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && hud.getState() && hud.blackHotbarValue.get()) {
            EntityPlayer entityPlayer = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date d= new Date();
            String str = sdf.format(d);
            int width = sr.getScaledWidth();
            DecimalFormat df = new DecimalFormat("0.00");
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GuiIngame.drawRect(0, sr.getScaledHeight() - 24, width, sr.getScaledHeight(), new Color(0, 0, 0,80).getRGB());
            GuiIngame.drawRect(0, sr.getScaledHeight() - 24, 3, sr.getScaledHeight(), ColorUtils.rainbow(400000000L).getRGB());
            FontManager.yahei20.drawStringWithShadow("X:"+df.format(Minecraft.getMinecraft().thePlayer.posX)+" Y:"+df.format(Minecraft.getMinecraft().thePlayer.posY)+" Z:"+df.format(Minecraft.getMinecraft().thePlayer.posZ) + " 用户名:" + Minecraft.getMinecraft().thePlayer.getGameProfile().getName(),5,sr.getScaledHeight() - 24,new Color(255,255,255).getRGB());
            Gui.drawRect(0,0,0,0,Color.black.getRGB());
            FontManager.yahei20.drawStringWithShadow(str + " FPS:" + Minecraft.getDebugFPS() + " Ping:" + EntityUtils.getPing(Minecraft.getMinecraft().thePlayer),5,sr.getScaledHeight() - 12,Color.white.getRGB());
            Gui.drawRect(0,0,0,0,Color.black.getRGB());
            GuiIngame.drawRect(width/2 - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 1, sr.getScaledHeight() - 24, width/2 - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 22, sr.getScaledHeight() - 22 - 1 + 24, Integer.MAX_VALUE);

            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for(int j = 0; j < 9; ++j) {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityPlayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

            HDebug.eventManager.callEvent(new Render2DEvent(partialTicks));
            callbackInfo.cancel();
        }

        if(Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && hud.getState() && hud.blackHotbar2Value.get()) {
            EntityPlayer entityPlayer = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d= new Date();
            String str = sdf.format(d);
            int width = sr.getScaledWidth();
            DecimalFormat df = new DecimalFormat("0.00");
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GuiIngame.drawRect(0, sr.getScaledHeight() - 24, width, sr.getScaledHeight(), new Color(0, 0, 0,80).getRGB());
            GuiIngame.drawRect(0, sr.getScaledHeight() - 24, 3, sr.getScaledHeight(), ColorUtils.rainbow(400000000L).getRGB());
            Fonts.minecraftFont.drawStringWithShadow("X:"+df.format(Minecraft.getMinecraft().thePlayer.posX)+" Y:"+df.format(Minecraft.getMinecraft().thePlayer.posY)+" Z:"+df.format(Minecraft.getMinecraft().thePlayer.posZ) + " UserName:" + Minecraft.getMinecraft().thePlayer.getGameProfile().getName(),5,sr.getScaledHeight() - 24,new Color(255,255,255).getRGB());
            Gui.drawRect(0,0,0,0,Color.black.getRGB());
            Fonts.minecraftFont.drawStringWithShadow(str + " FPS:" + Minecraft.getDebugFPS() + " Ping:" + EntityUtils.getPing(Minecraft.getMinecraft().thePlayer),5,sr.getScaledHeight() - 12,Color.white.getRGB());
            Gui.drawRect(0,0,0,0,Color.black.getRGB());
            GuiIngame.drawRect(width/2 - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 1, sr.getScaledHeight() - 24, width/2 - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 22, sr.getScaledHeight() - 22 - 1 + 24, Integer.MAX_VALUE);

            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for(int j = 0; j < 9; ++j) {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityPlayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

            HDebug.eventManager.callEvent(new Render2DEvent(partialTicks));
            callbackInfo.cancel();
        }
        if(Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && hud.getState() && hud.blackHotbarNoTextValue.get()) {
            EntityPlayer entityPlayer = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();
            int width = sr.getScaledWidth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GuiIngame.drawRect(0, sr.getScaledHeight() - 24, width, sr.getScaledHeight(), new Color(0, 0, 0,80).getRGB());
            GuiIngame.drawRect(0, sr.getScaledHeight() - 24, 3, sr.getScaledHeight(), ColorUtils.rainbow(400000000L).getRGB());
            Gui.drawRect(0,0,0,0,Color.black.getRGB());
            Gui.drawRect(0,0,0,0,Color.black.getRGB());
            GuiIngame.drawRect(width/2 - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 1, sr.getScaledHeight() - 24, width/2 - 91 - 1 + entityPlayer.inventory.currentItem * 20 + 22, sr.getScaledHeight() - 22 - 1 + 24, Integer.MAX_VALUE);

            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for(int j = 0; j < 9; ++j) {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityPlayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

            HDebug.eventManager.callEvent(new Render2DEvent(partialTicks));
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderTooltip", at = @At("RETURN"))
    private void renderTooltipPost(ScaledResolution sr, float partialTicks, CallbackInfo callbackInfo) {
        if(!ClassUtils.hasClass("net.labymod.api.LabyModAPI"))
            HDebug.eventManager.callEvent(new Render2DEvent(partialTicks));
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPumpkinOverlay(final CallbackInfo callbackInfo) {
        final AntiBlind antiBlind = (AntiBlind) HDebug.moduleManager.getModule(AntiBlind.class);

        if(antiBlind.getState() && antiBlind.getPumpkinEffect().get())
            callbackInfo.cancel();
    }
}