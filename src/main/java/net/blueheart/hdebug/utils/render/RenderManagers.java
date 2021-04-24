package net.blueheart.hdebug.utils.render;

import com.google.common.collect.Maps;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Map;

public class RenderManagers {
    /** A map of entity classes and the associated renderer. */
    private Map entityRenderMap = Maps.newHashMap();

    /**
     * lists the various player skin types with their associated Renderer class instances.
     */
    private Map skinMap = Maps.newHashMap();
    private RenderPlayer playerRenderer;

    /** Renders fonts */
    private FontRenderer textRenderer;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public TextureManager renderEngine;

    /** Reference to the World object. */
    public World worldObj;

    /** Rendermanager's variable for the player */
    public Entity livingPlayer;
    public Entity pointedEntity;
    public static float playerViewY;
    public float playerViewX;

    /** Reference to the GameSettings object. */
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    private boolean renderOutlines = false;
    private boolean renderShadow = true;

    /** whether bounding box should be rendered or not */
    private boolean debugBoundingBox = false;
    private static final String __OBFID = "CL_00000991";


    public void setRenderPosition(double renderPosXIn, double renderPosYIn, double renderPosZIn)
    {
        this.renderPosX = renderPosXIn;
        this.renderPosY = renderPosYIn;
        this.renderPosZ = renderPosZIn;
    }

    public Render getEntityClassRenderObject(Class p_78715_1_)
    {
        Render render = (Render)this.entityRenderMap.get(p_78715_1_);

        if (render == null && p_78715_1_ != Entity.class)
        {
            render = this.getEntityClassRenderObject(p_78715_1_.getSuperclass());
            this.entityRenderMap.put(p_78715_1_, render);
        }

        return render;
    }

    public Render getEntityRenderObject(Entity entityIn)
    {
        if (entityIn instanceof AbstractClientPlayer)
        {
            String s = ((AbstractClientPlayer)entityIn).getSkinType();
            RenderPlayer renderplayer = (RenderPlayer)this.skinMap.get(s);
            return renderplayer != null ? renderplayer : this.playerRenderer;
        }
        else
        {
            return this.getEntityClassRenderObject(entityIn.getClass());
        }
    }


    public void setPlayerViewY(float playerViewYIn)
    {
        this.playerViewY = playerViewYIn;
    }

    public boolean isRenderShadow()
    {
        return this.renderShadow;
    }

    public void setRenderShadow(boolean renderShadowIn)
    {
        this.renderShadow = renderShadowIn;
    }

    public void setDebugBoundingBox(boolean debugBoundingBoxIn)
    {
        this.debugBoundingBox = debugBoundingBoxIn;
    }

    public boolean isDebugBoundingBox()
    {
        return this.debugBoundingBox;
    }

    public boolean renderEntitySimple(Entity entityIn, float partialTicks)
    {
        return this.renderEntityStatic(entityIn, partialTicks, false);
    }

    public boolean shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ)
    {
        Render render = this.getEntityRenderObject(entityIn);
        return render != null && render.shouldRender(entityIn, camera, camX, camY, camZ);
    }

    public boolean renderEntityStatic(Entity entity, float partialTicks, boolean p_147936_3_)
    {
        if (entity.ticksExisted == 0)
        {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
        }

        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        int i = entity.getBrightnessForRender(partialTicks);

        if (entity.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        return this.doRenderEntity(entity, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ, f, partialTicks, p_147936_3_);
    }


    public boolean renderEntityWithPosYaw(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks)
    {
        return this.doRenderEntity(entityIn, x, y, z, entityYaw, partialTicks, false);
    }

    public boolean doRenderEntity(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean p_147939_10_)
    {
        Render render = null;

        try
        {
            render = this.getEntityRenderObject(entity);

            if (render != null && this.renderEngine != null)
            {
                try
                {
                    if (render instanceof RendererLivingEntity)
                    {
                        ((RendererLivingEntity)render).setRenderOutlines(this.renderOutlines);
                    }

                    render.doRender(entity, x, y, z, entityYaw, partialTicks);
                }
                catch (Throwable throwable2)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
                }

                try
                {
                    if (!this.renderOutlines)
                    {
                        render.doRenderShadowAndFire(entity, x, y, z, entityYaw, partialTicks);
                    }
                }
                catch (Throwable throwable1)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
                }

                if (this.debugBoundingBox && !entity.isInvisible() && !p_147939_10_)
                {
                    try
                    {
                        this.renderDebugBoundingBox(entity, x, y, z, entityYaw, partialTicks);
                    }
                    catch (Throwable throwable)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }
            }
            else if (this.renderEngine != null)
            {
                return false;
            }

            return true;
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            entity.addEntityCrashInfo(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addCrashSection("Assigned renderer", render);
            crashreportcategory1.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
            crashreportcategory1.addCrashSection("Rotation", Float.valueOf(entityYaw));
            crashreportcategory1.addCrashSection("Delta", Float.valueOf(partialTicks));
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Renders the bounding box around an entity when F3+B is pressed
     */
    private void renderDebugBoundingBox(Entity entityIn, double p_85094_2_, double p_85094_4_, double p_85094_6_, float p_85094_8_, float p_85094_9_)
    {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        float f = entityIn.width / 2.0F;
        AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX - entityIn.posX + p_85094_2_, axisalignedbb.minY - entityIn.posY + p_85094_4_, axisalignedbb.minZ - entityIn.posZ + p_85094_6_, axisalignedbb.maxX - entityIn.posX + p_85094_2_, axisalignedbb.maxY - entityIn.posY + p_85094_4_, axisalignedbb.maxZ - entityIn.posZ + p_85094_6_);
        RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, 255, 255, 255, 255);

        if (entityIn instanceof EntityLivingBase)
        {
            float f1 = 0.01F;
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(p_85094_2_ - (double)f, p_85094_4_ + (double)entityIn.getEyeHeight() - 0.009999999776482582D, p_85094_6_ - (double)f, p_85094_2_ + (double)f, p_85094_4_ + (double)entityIn.getEyeHeight() + 0.009999999776482582D, p_85094_6_ + (double)f), 255, 0, 0, 255);
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        Vec3 vec3 = entityIn.getLook(p_85094_9_);
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(p_85094_2_, p_85094_4_ + (double)entityIn.getEyeHeight(), p_85094_6_).color(0, 0, 255, 255).endVertex();
        worldrenderer.pos(p_85094_2_ + vec3.xCoord * 2.0D, p_85094_4_ + (double)entityIn.getEyeHeight() + vec3.yCoord * 2.0D, p_85094_6_ + vec3.zCoord * 2.0D).color(0, 0, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    /**
     * World sets this RenderManager's worldObj to the world provided
     */
    public void set(World worldIn)
    {
        this.worldObj = worldIn;
    }

    public double getDistanceToCamera(double p_78714_1_, double p_78714_3_, double p_78714_5_)
    {
        double d0 = p_78714_1_ - this.viewerPosX;
        double d1 = p_78714_3_ - this.viewerPosY;
        double d2 = p_78714_5_ - this.viewerPosZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Returns the font renderer
     */
    public FontRenderer getFontRenderer()
    {
        return this.textRenderer;
    }

    public void setRenderOutlines(boolean renderOutlinesIn)
    {
        this.renderOutlines = renderOutlinesIn;
    }

    public Map getEntityRenderMap()
    {
        return this.entityRenderMap;
    }

    public void setEntityRenderMap(Map p_setEntityRenderMap_1_)
    {
        this.entityRenderMap = p_setEntityRenderMap_1_;
    }

    public Map<String, RenderPlayer> getSkinMap()
    {
        return Collections.<String, RenderPlayer>unmodifiableMap(this.skinMap);
    }
}