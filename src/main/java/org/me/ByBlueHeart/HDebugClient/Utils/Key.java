package org.me.ByBlueHeart.HDebugClient.Utils;

import net.blueheart.hdebug.ui.font.FontManager;
import net.blueheart.hdebug.ui.font.UnicodeFontRenderer;
import net.blueheart.hdebug.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.me.ByBlueHeart.HDebugClient.Modules.Render.Keyrender;

import java.awt.*;

public class Key {

    private final KeyBinding key;

    private final int xOffset;

    private final int yOffset;

    private boolean wasPressed = true;

    private long lastPress = 0L;

    private int color = 255;

    private double textBrightness = 1.0D;

    UnicodeFontRenderer font = FontManager.yahei18;

    public Key(KeyBinding key, int xOffset, int yOffset) {
        this.key = key;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void renderKey(int x, int y, int textColor) {
        boolean pressed = this.key.isKeyDown();
        String name = Keyboard.getKeyName(this.key.getKeyCode());
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
        if (((Boolean) Keyrender.BackGroundRainbow.get()).booleanValue())
            Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + (name.equals("SPACE") ? 70 : 22), y + this.yOffset + (name.equals("SPACE") ? 11 : 22), Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
        Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + (name.equals("SPACE") ? 70 : 22), y + this.yOffset + (name.equals("SPACE") ? 11 : 22), 2013265920 + (this.color << 16) + (this.color << 8) + this.color);
        if (((Boolean) Keyrender.Rect.get()).booleanValue()) {
            Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + (name.equals("SPACE") ? 70 : 22), y + this.yOffset + (name.equals("SPACE") ? 1 : 1), !((Boolean)Keyrender.RectRainbow.get()).booleanValue() ? Color.WHITE.getRGB() : Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
            Gui.drawRect(x + this.xOffset, y + this.yOffset, x + this.xOffset + (name.equals("SPACE") ? 1 : 1), y + this.yOffset + (name.equals("SPACE") ? 11 : 22), !((Boolean)Keyrender.RectRainbow.get()).booleanValue() ? Color.WHITE.getRGB() : Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
            Gui.drawRect(x + this.xOffset, y + this.yOffset + (name.equals("SPACE") ? 10 : 21), x + this.xOffset + (name.equals("SPACE") ? 70 : 22), y + this.yOffset + (name.equals("SPACE") ? 11 : 22), !((Boolean)Keyrender.RectRainbow.get()).booleanValue() ? Color.WHITE.getRGB() : Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
            Gui.drawRect(x + this.xOffset + (name.equals("SPACE") ? 69 : 21), y + this.yOffset, x + this.xOffset + (name.equals("SPACE") ? 70 : 22), y + this.yOffset + (name.equals("SPACE") ? 11 : 22), !((Boolean)Keyrender.RectRainbow.get()).booleanValue() ? Color.WHITE.getRGB() : Keyrender.Rainbow.getColorValue((Keyrender.saturation.get()).floatValue(), (Keyrender.brightness.get()).floatValue(), (Keyrender.RainbowValue.get()).floatValue()).getRGB());
        }
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        float nameWidth = this.font.getStringWidth(name);
        if (name.equals("SPACE")) {
            RenderUtils.drawHLine((x + this.xOffset + 18), (y + this.yOffset + 5), (x + this.xOffset + 72 - 18), (y + this.yOffset + 5), 1.0F, textColor);
        } else {
            this.font.drawString(name, (int) ((x + this.xOffset + (name.equals("SPACE") ? 72 : 24) / 2) - nameWidth / 2.0F), (y + this.yOffset + (name.equals("SPACE") ? 3 : 8)), -16777216 + ((int)(red * this.textBrightness) << 16) + ((int)(green * this.textBrightness) << 8) + (int)(blue * this.textBrightness));
        }
    }
}