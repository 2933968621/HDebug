package net.blueheart.hdebug.features.module.modules.render;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.ui.client.clickgui.style.styles.HDebugStyle;
import net.blueheart.hdebug.ui.client.clickgui.style.styles.LiquidBounceStyle;
import net.blueheart.hdebug.ui.client.clickgui.style.styles.NullStyle;
import net.blueheart.hdebug.ui.client.clickgui.style.styles.SlowlyStyle;
import net.blueheart.hdebug.utils.render.ColorUtils;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.IntegerValue;
import net.blueheart.hdebug.value.ListValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", description = "Opens the ClickGUI.", category = ModuleCategory.RENDER, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUI extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[] {"LiquidBounce", "Null", "Slowly", "HDebug"}, "HDebug") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };

    public final FloatValue scaleValue = new FloatValue("Scale", 1F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    private static final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private static final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private static final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private static final BoolValue colorRainbow = new BoolValue("Rainbow", false);

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    @Override
    public void onEnable() {
        updateStyle();

        mc.displayGuiScreen(HDebug.clickGui);
    }

    private void updateStyle() {
        switch(styleValue.get().toLowerCase()) {
            case "liquidbounce":
                HDebug.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                HDebug.clickGui.style = new NullStyle();
                break;
            case "slowly":
                HDebug.clickGui.style = new SlowlyStyle();
                break;
            case "hdebug":
                HDebug.clickGui.style = new HDebugStyle();
                break;
        }
    }
}
