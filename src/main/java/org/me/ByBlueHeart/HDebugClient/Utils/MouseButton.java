/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package org.me.ByBlueHeart.HDebugClient.Utils;

import net.blueheart.hdebug.ui.font.FontManager;
import net.blueheart.hdebug.ui.font.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.me.ByBlueHeart.HDebugClient.Modules.Render.Keyrender;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MouseButton {
    private static final String[] BUTTONS = new String[] { "LMB", "RMB" };

    private final int button;

    private final int xOffset;

    private final int yOffset;

    private List<Long> clicks = new ArrayList<>();

    private boolean wasPressed = true;

    private long lastPress = 0L;

    private int color = 255;

    private double textBrightness = 1.0D;

    UnicodeFontRenderer font = FontManager.yahei18;

    UnicodeFontRenderer fontS = FontManager.yahei12;

    public MouseButton(int button, int xOffset, int yOffset) {
        this.button = button;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getClicks() {
        Iterator<Long> iterator = this.clicks.iterator();
        while (iterator.hasNext()) {
            if (((Long)iterator.next()).longValue() >= System.currentTimeMillis() - 1000L)
                continue;
            iterator.remove();
        }
        return this.clicks.size();
    }

    public void renderMouseButton(int x, int y, int textColor) {
        boolean pressed = Mouse.isButtonDown(this.button);
        String name = BUTTONS[this.button];
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
        }
        if (pressed) {
            this.color = Math.min(255, (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.max(0.0D, 1.0D - (System.currentTimeMillis() - this.lastPress) / 20.0D);
        } else {
            this.color = Math.max(0, 255 - (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.min(1.0D, (System.currentTimeMillis() - this.lastPress) / 20.0D);
        }
        if ((Keyrender.BackGroundRainbow.get()).booleanValue())
            Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 34, y + this.yOffset + 22, Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
        Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 34, y + this.yOffset + 22, 2013265920 + (this.color << 16) + (this.color << 8) + this.color);
        if ((Keyrender.Rect.get()).booleanValue()) {
            Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 34, y + this.yOffset + 1, !(Keyrender.RectRainbow.get()).booleanValue() ? Color.WHITE.getRGB() : Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
            Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + 1, y + this.yOffset + 22, !(Keyrender.RectRainbow.get()).booleanValue() ? Color.WHITE.getRGB() : Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
            Gui.drawRect(x + this.xOffset, y + this.yOffset + 21, x + this.xOffset + 34, y + this.yOffset + 22, !(Keyrender.RectRainbow.get()).booleanValue() ? Color.WHITE.getRGB() : Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
            Gui.drawRect(x + this.xOffset + 33, y + this.yOffset, x + this.xOffset + 34, y + this.yOffset + 22, !(Keyrender.RectRainbow.get()).booleanValue() ? Color.WHITE.getRGB() : Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
        }
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        if (Keyrender.mode.get() == "low") {
            float nameWidth = this.font.getStringWidth(name);
            this.font.drawString(name, (int) ((x + this.xOffset + 18) - nameWidth / 2.0F), (y + this.yOffset + 5), -16777216 + ((int)(red * this.textBrightness) << 16) + ((int)(green * this.textBrightness) << 8) + (int)(blue * this.textBrightness));
            String cpsText = String.valueOf(String.valueOf(getClicks())) + " CPS";
            float cpsTextWidth = this.fontS.getStringWidth(cpsText);
            this.fontS.drawString(cpsText, (int) ((x + this.xOffset + 18) - cpsTextWidth / 2.0F), (y + this.yOffset + 14), -16777216 + ((int)(255.0D * this.textBrightness) << 16) + ((int)(255.0D * this.textBrightness) << 8) + (int)(255.0D * this.textBrightness));
        } else if (Keyrender.mode.get() == "click") {
            if (getClicks() == 0) {
                float nameWidth = this.font.getStringWidth(name);
                this.font.drawString(name, (int) ((x + this.xOffset + 18) - nameWidth / 2.0F), (y + this.yOffset + 12 - 4), -16777216 + ((int)(red * this.textBrightness) << 16) + ((int)(green * this.textBrightness) << 8) + (int)(blue * this.textBrightness));
            } else {
                String cpsText = String.valueOf(String.valueOf(getClicks())) + " CPS";
                float cpsTextWidth = this.font.getStringWidth(cpsText);
                this.font.drawString(cpsText, (int) ((x + this.xOffset + 18) - cpsTextWidth / 2.0F), (y + this.yOffset + 12 - 4), -16777216 + ((int)(red * this.textBrightness) << 16) + ((int)(green * this.textBrightness) << 8) + (int)(blue * this.textBrightness));
            }
        }
    }
}