package org.me.ByBlueHeart.HDebugClient.Modules.Render;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.Render2DEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import org.me.ByBlueHeart.HDebugClient.Utils.Key;
import org.me.ByBlueHeart.HDebugClient.Utils.MouseButton;
import org.me.ByBlueHeart.HDebugClient.Utils.RainbowUtils;

import java.awt.*;

@ModuleInfo(name = "KeyStrokes", description = "Key Strokes.", category = ModuleCategory.RENDER)
public class Keyrender extends Module {
    private static final int[] COLORS = new int[] { 16777215, 16711680, 65280, 255, 16776960, 11141290 };
    private final Key[] movementKeys = new Key[5];
    private final MouseButton[] mouseButtons = new MouseButton[2];
    public static ListValue mode = new ListValue("Mode", new String[]{"low","click"}, "low");
    public static BoolValue BackGroundRainbow = new BoolValue("BackGroundRainbow",  false);
    public static BoolValue RectRainbow = new BoolValue("RectRainbow",  true);
    public static BoolValue Rect = new BoolValue("Rect",  true);
    public static FloatValue saturation = new FloatValue("saturation",0.5F, 0.1F, 1.0F);
    public static FloatValue brightness = new FloatValue("brightness", 0.9F, 0.1F, 1.0F);
    public static FloatValue RainbowValue = new FloatValue("RainbowValue",10.0F, 1.0F, 50.0F);
    public static FloatValue RainbowSpeed = new FloatValue("RainbowSpeed",  5.0F, 1.0F, 50.0F);
    public static FloatValue X = new FloatValue("X",15.0F, 0.0F, 1000.0F);
    public static FloatValue Y = new FloatValue("Y", 170.0F, 0.0F, 1000.0F);
    public static RainbowUtils Rainbow = new RainbowUtils();

    @Override
    public void onEnable(){
        movementKeys[0] = new Key(Module.mc.gameSettings.keyBindForward, 26, 2);
        movementKeys[1] = new Key(Module.mc.gameSettings.keyBindLeft, 2, 26);
        movementKeys[2] = new Key(Module.mc.gameSettings.keyBindBack, 26, 26);
        movementKeys[3] = new Key(Module.mc.gameSettings.keyBindRight, 50, 26);
        mouseButtons[0] = new MouseButton(0, 2, 50);
        mouseButtons[1] = new MouseButton(1, 38, 50);
        movementKeys[4] = new Key(Module.mc.gameSettings.keyBindJump, 2, 74);
    }

    private void draw(int x, int y, int textColor) {
        movementKeys[0].renderKey(x, y, textColor);
        movementKeys[1].renderKey(x, y, textColor);
        movementKeys[2].renderKey(x, y, textColor);
        movementKeys[3].renderKey(x, y, textColor);
        mouseButtons[0].renderMouseButton(x, y, textColor);
        mouseButtons[1].renderMouseButton(x, y, textColor);
        movementKeys[4].renderKey(x, y, textColor);
    }

    @EventTarget
    public void renderKeystrokes(Render2DEvent e) {
        int x = X.get().intValue();
        int y = Y.get().intValue();
        int textColor = Color.WHITE.getRGB();
        new ScaledResolution(Module.mc);
        draw(x, y, textColor);
        Rainbow.addValue(0.2f * RainbowSpeed.get().floatValue());
    }
}