
package net.blueheart.hdebug.injection.forge.mixins.gui;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.features.module.modules.misc.ComponentOnHover;
import org.me.ByBlueHeart.HDebugClient.Modules.Render.HUD;
import net.blueheart.hdebug.ui.client.GuiBackground;
import net.blueheart.hdebug.utils.render.ParticleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mixin(GuiScreen.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiScreen {
    int random = new Random().nextInt(3);

    @Shadow
    public Minecraft mc;

    @Shadow
    protected List<GuiButton> buttonList;

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Shadow
    protected FontRenderer fontRendererObj;

    @Shadow
    public void updateScreen() {
    }

    @Shadow
    public abstract void handleComponentHover(IChatComponent component, int x, int y);

    @Shadow
    protected abstract void drawHoveringText(List<String> textLines, int x, int y);

    @Inject(method = "drawWorldBackground", at = @At("HEAD"))
    private void drawWorldBackground(final CallbackInfo callbackInfo) {
        final HUD hud = (HUD) HDebug.moduleManager.getModule(HUD.class);
        if(hud.inventoryParticle.get() && mc.thePlayer != null) {
            final ScaledResolution scaledResolution = new ScaledResolution(mc);
            final int width = scaledResolution.getScaledWidth();
            final int height = scaledResolution.getScaledHeight();
            ParticleUtils.drawParticles(Mouse.getX() * width / mc.displayWidth, height - Mouse.getY() * height / mc.displayHeight - 1);
        }
    }

    /**
     * @author CCBlueX
     */
    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void drawClientBackground(final CallbackInfo callbackInfo) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        if(GuiBackground.Companion.getEnabled()) {
            if (HDebug.INSTANCE.getBackground() == null) {
                //BackgroundShader.BACKGROUND_SHADER.startShader();
                //final Tessellator instance = Tessellator.getInstance();
                //final WorldRenderer worldRenderer = instance.getWorldRenderer();
                //worldRenderer.begin(7, DefaultVertexFormats.POSITION);
                //worldRenderer.pos(0, height, 0.0D).endVertex();
                //worldRenderer.pos(width, height, 0.0D).endVertex();
                //worldRenderer.pos(width, 0, 0.0D).endVertex();
                //worldRenderer.pos(0, 0, 0.0D).endVertex();
                //instance.draw();
                //BackgroundShader.BACKGROUND_SHADER.stopShader();
                final ScaledResolution scaledResolution = new ScaledResolution(mc);
                final int width = scaledResolution.getScaledWidth();
                final int height = scaledResolution.getScaledHeight();
                mc.getTextureManager().bindTexture(new ResourceLocation(HDebug.CLIENT_NAME.toLowerCase() + "/background/background"+random+".png"));
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, width, height, width, height, width, height);
            }else{
                final ScaledResolution scaledResolution = new ScaledResolution(mc);
                final int width = scaledResolution.getScaledWidth();
                final int height = scaledResolution.getScaledHeight();
                mc.getTextureManager().bindTexture(HDebug.INSTANCE.getBackground());
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, width, height, width, height, width, height);
            }
            if (GuiBackground.Companion.getParticles())
                ParticleUtils.drawParticles(Mouse.getX() * width / mc.displayWidth, height - Mouse.getY() * height / mc.displayHeight - 1);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "drawBackground", at = @At("RETURN"))
    private void drawParticles(final CallbackInfo callbackInfo) {
        if(GuiBackground.Companion.getParticles())
            ParticleUtils.drawParticles(Mouse.getX() * width / mc.displayWidth, height - Mouse.getY() * height / mc.displayHeight - 1);
    }

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void messageSend(String msg, boolean addToChat, final CallbackInfo callbackInfo) {
        if (msg.startsWith(String.valueOf(HDebug.commandManager.getPrefix())) && addToChat) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);

            HDebug.commandManager.executeCommands(msg);
            callbackInfo.cancel();
        }
    }

    @Inject(method = "handleComponentHover", at = @At("HEAD"))
    private void handleHoverOverComponent(IChatComponent component, int x, int y, final CallbackInfo callbackInfo) {
        if (component == null || component.getChatStyle().getChatClickEvent() == null || !HDebug.moduleManager.getModule(ComponentOnHover.class).getState())
            return;

        final ChatStyle chatStyle = component.getChatStyle();

        final ClickEvent clickEvent = chatStyle.getChatClickEvent();
        final HoverEvent hoverEvent = chatStyle.getChatHoverEvent();

        drawHoveringText(Collections.singletonList("§c§l" + clickEvent.getAction().getCanonicalName().toUpperCase() + ": §a" + clickEvent.getValue()), x, y - (hoverEvent != null ? 17 : 0));
    }
}