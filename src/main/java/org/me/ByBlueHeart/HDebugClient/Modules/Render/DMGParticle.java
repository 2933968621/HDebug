package org.me.ByBlueHeart.HDebugClient.Modules.Render;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.Render3DEvent;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.Aura;
import org.me.ByBlueHeart.HDebugClient.API.Location;
import org.me.ByBlueHeart.HDebugClient.API.Particles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@ModuleInfo(name = "DMGParticle", description = "Allows you to see items through walls.", category = ModuleCategory.RENDER)
public class DMGParticle extends Module {
    private Minecraft mc = Minecraft.getMinecraft();

    private HashMap<EntityLivingBase, Float> healthMap = new HashMap<>();

    private List<Particles> particles = new ArrayList<>();

    @EventTarget
    public void onLivingUpdate(UpdateEvent e) {
        Aura a = (Aura) HDebug.moduleManager.getModule(Aura.class);
        EntityLivingBase entity = a.getTarget();
        if (entity == this.mc.thePlayer) {
            return;
        }
        if (!this.healthMap.containsKey(entity)) {
            this.healthMap.put(entity, entity.getHealth());
        }
        float floatValue = this.healthMap.get(entity);
        float health = entity.getHealth();
        if (floatValue != health) {
            String text;
            if (floatValue - health < 0.0f) {
                text = "\247a" + roundToPlace((floatValue - health) * -1.0f, 1);
            }else {
                text = "\247e" + roundToPlace(floatValue - health, 1);
            }
            Location location = new Location(entity);
            location.setY(entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
            location.setX(location.getX() - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
            location.setZ(location.getZ() - 0.5 + new Random(System.currentTimeMillis() + 1).nextInt(5) * 0.1);
            this.particles.add(new Particles(location, text));
            this.healthMap.remove(entity);
            this.healthMap.put(entity, entity.getHealth());
        }
    }

    @EventTarget
    public void onRender(Render3DEvent e) {
        for (Particles p : this.particles) {
            float textY;
            double x = p.location.getX();
            this.mc.getRenderManager();
            double n = x - (this.mc.getRenderManager()).renderPosX;
            double y = p.location.getY();
            this.mc.getRenderManager();
            double n2 = y - (this.mc.getRenderManager()).renderPosY;
            double z = p.location.getZ();
            this.mc.getRenderManager();
            double n3 = z - (this.mc.getRenderManager()).renderPosZ;
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
            GlStateManager.translate((float)n, (float)n2, (float)n3);
            GlStateManager.rotate(-(this.mc.getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
            if (this.mc.gameSettings.thirdPersonView == 2) {
                textY = -1.0F;
            } else {
                textY = 1.0F;
            }
            GlStateManager.rotate((this.mc.getRenderManager()).playerViewX, textY, 0.0F, 0.0F);
            double size = 0.03D;
            GlStateManager.scale(-0.03D, -0.03D, 0.03D);
            enableGL2D();
            disableGL2D();
            GL11.glDepthMask(false);
            this.mc.fontRendererObj.drawStringWithShadow(p.text, -(this.mc.fontRendererObj.getStringWidth(p.text) / 2), -(this.mc.fontRendererObj.FONT_HEIGHT - 1), 0);
            GL11.glColor4f(187F, 255F, 255F, 1.0F);
            GL11.glDepthMask(true);
            GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.popMatrix();
        }
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static double roundToPlace(double p_roundToPlace_0_, int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0)
            throw new IllegalArgumentException();
        return (new BigDecimal(p_roundToPlace_0_)).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        try{
            this.particles.forEach(update -> {
                update.ticks++;
                if (update.ticks <= 10)
                    update.location.setY(update.location.getY() + update.ticks * 0.005D);
                if (update.ticks > 20)
                    this.particles.remove(update);
            });
        }catch (Throwable ignored){

        }
    }
}
