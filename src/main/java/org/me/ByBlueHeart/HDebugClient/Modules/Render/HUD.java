package org.me.ByBlueHeart.HDebugClient.Modules.Render;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.*;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.ui.client.hud.designer.GuiHudDesigner;
import net.blueheart.hdebug.utils.EntityUtils;
import net.blueheart.hdebug.utils.render.ColorUtils;
import net.blueheart.hdebug.value.BoolValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER)
@SideOnly(Side.CLIENT)
public class HUD extends Module {

    public final BoolValue blackHotbarValue = new BoolValue("BlackHotbar", true);
    public final BoolValue blackHotbar2Value = new BoolValue("BlackHotBar2", false);
    public final BoolValue blackHotbarNoTextValue = new BoolValue("BlackHotBarNoText", false);
    //public static final BoolValue ChineseValue = new BoolValue("Chinese", false);
    public final BoolValue inventoryParticle = new BoolValue("InventoryParticle", false);
    private final BoolValue blurValue = new BoolValue("Blur", false);
    private final BoolValue infoValue = new BoolValue("Info", true);
    public final BoolValue fontChatValue = new BoolValue("FontChat", false);

    public HUD() {
        setState(true);
        setArray(false);
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (mc.currentScreen instanceof GuiHudDesigner)
            return;

        if(infoValue.get()){
            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
            Date d = new Date();
            String str = time.format(d);
            String text = EnumChatFormatting.RED + "H" +
                    EnumChatFormatting.RESET + "Debug.dll | FPS [ " +
                    EnumChatFormatting.GREEN + mc.getDebugFPS() + EnumChatFormatting.RESET + " ] | Ping [ " +
                    EnumChatFormatting.AQUA + EntityUtils.getPing(mc.thePlayer) + EnumChatFormatting.RESET + " ] | " +
                    EnumChatFormatting.YELLOW + str + EnumChatFormatting.BLUE;
            Gui.drawRect(5, 9, mc.fontRendererObj.getStringWidth(text) + 14,19, new Color(0,0,0).getRGB());
            Gui.drawRect(5, 5, mc.fontRendererObj.getStringWidth(text) + 14,9, ColorUtils.rainbow(400000000L).getRGB());
            mc.fontRendererObj.drawString(text, 9, 10, new Color(255, 255, 255).getRGB());
        }
        HDebug.hud.render(false);
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        HDebug.hud.update();
    }

    @EventTarget
    public void onKey(final KeyEvent event) {
        HDebug.hud.handleKey('a', event.getKey());
    }

    @EventTarget(ignoreCondition = true)
    public void onScreen(final ScreenEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;

        if (getState() && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.getGuiScreen() != null &&
                !(event.getGuiScreen() instanceof GuiChat || event.getGuiScreen() instanceof GuiHudDesigner))
            mc.entityRenderer.loadShader(new ResourceLocation(HDebug.CLIENT_NAME.toLowerCase() + "/blur.json"));
        else if (mc.entityRenderer.getShaderGroup() != null &&
                mc.entityRenderer.getShaderGroup().getShaderGroupName().contains("hdebug/blur.json"))
            mc.entityRenderer.stopUseShader();
    }
}