package org.me.ByBlueHeart.HDebugClient.Modules.Render;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.Render2DEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.Aura;
import net.blueheart.hdebug.utils.Colors;
import net.blueheart.hdebug.utils.render.RenderUtils;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

@ModuleInfo(name = "TargetHUD2", description = "HUD your Target Player.", category = ModuleCategory.RENDER)
public class NewTargetHUD
        extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[] { "Astro", "AimWhere" }, "Astro");
    Aura killAura = (Aura) HDebug.moduleManager.getModule(Aura.class);

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        EntityLivingBase entityLivingBase = this.killAura.getTarget();
        if (entityLivingBase instanceof EntityLivingBase &&
                entityLivingBase != null && !((Entity)entityLivingBase).isDead && entityLivingBase instanceof net.minecraft.entity.player.EntityPlayer && mc.thePlayer.getDistanceToEntity((Entity)entityLivingBase) < 8.0F) {
            double hpPercentage = (entityLivingBase.getHealth() / entityLivingBase.getMaxHealth());
            ScaledResolution scaledRes = new ScaledResolution(mc);
            float scaledWidth = scaledRes.getScaledWidth();
            float scaledHeight = scaledRes.getScaledHeight();
            if (hpPercentage > 1.0D) {
                hpPercentage = 1.0D;
            } else if (hpPercentage < 0.0D) {
                hpPercentage = 0.0D;
            }
            RenderUtils.rectangleBordered((scaledWidth / 2.0F - 200.0F), (scaledHeight / 2.0F - 42.0F), (scaledWidth / 2.0F - 200.0F + 40.0F), (scaledHeight / 2.0F - 2.0F), 1.0D, Colors.getColor(0, 0), Colors.getColor(0, 0));
            RenderUtils.drawRect(scaledWidth / 2.0F - 200.0F, scaledHeight / 2.0F - 42.0F, scaledWidth / 2.0F - 200.0F + 40.0F + ((mc.fontRendererObj.getStringWidth(entityLivingBase.getName()) > 105) ? (mc.fontRendererObj.getStringWidth(entityLivingBase.getName()) - 10) : 105), scaledHeight / 2.0F - 2.0F, (new Color(34, 34, 34, 150)).getRGB());
            RenderUtils.drawFace((int)scaledWidth / 2 - 196, (int)(scaledHeight / 2.0F - 38.0F), 8.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F, (AbstractClientPlayer)entityLivingBase);
            mc.fontRendererObj.drawStringWithShadow(entityLivingBase.getName(), scaledWidth / 2.0F - 196.0F + 40.0F, scaledHeight / 2.0F - 36.0F, -1);
            RenderUtils.drawRect((scaledWidth / 2.0F - 196.0F + 40.0F), (scaledHeight / 2.0F - 26.0F), (float) ((scaledWidth / 2.0F - 196.0F + 40.0F) + 87.5D), (scaledHeight / 2.0F - 14.0F), (new Color(55, 55, 55)).getRGB());
            RenderUtils.drawRect((scaledWidth / 2.0F - 196.0F + 40.0F), (scaledHeight / 2.0F - 26.0F), (float) ((scaledWidth / 2.0F - 196.0F + 40.0F) + hpPercentage * 1.25D * 70.0D), (scaledHeight / 2.0F - 14.0F), Colors.getHealthColor(entityLivingBase).getRGB());
            mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", new Object[] { Float.valueOf(entityLivingBase.getHealth()) }), scaledWidth / 2.0F - 196.0F + 40.0F + 36.0F, scaledHeight / 2.0F - 23.0F, Colors.getHealthColor(entityLivingBase).getRGB());
            mc.fontRendererObj.drawStringWithShadow("Distance: \u00A77" + (int)mc.thePlayer.getDistanceToEntity((Entity)entityLivingBase) + "m", scaledWidth / 2.0F - 196.0F + 40.0F, scaledHeight / 2.0F - 12.0F, -1);
        }
    }
}