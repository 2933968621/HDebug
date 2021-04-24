package org.me.ByBlueHeart.HDebugClient.Modules.Render;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.ChamsColor;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name = "Chams", description = "Allows you to see targets through blocks.", category = ModuleCategory.RENDER)
public final class Chams extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[] {"Color", "Textured"}, "Textured"); @NotNull public final ListValue ModeValue() { return this.modeValue; }
    private final FloatValue colorRedValue = new FloatValue("R", 100.0F, 0.0F, 255.0F);
    private final FloatValue colorGreenValue = new FloatValue("G", 100.0F, 0.0F, 255.0F);
    private final FloatValue colorBlueValue = new FloatValue("B", 72.0F, 0.0F, 255.0F);
    private final FloatValue colorAValue = new FloatValue("A", 50.0F, 0.0F, 255.0F); @NotNull
    private final BoolValue targetsValue = new BoolValue("Targets", true); @NotNull public final BoolValue getTargetsValue() { return this.targetsValue; } @NotNull
    private final BoolValue chestsValue = new BoolValue("Chests", true); @NotNull public final BoolValue getChestsValue() { return this.chestsValue; } @NotNull
    private final BoolValue itemsValue = new BoolValue("Items", true); @NotNull public final BoolValue getItemsValue() { return this.itemsValue; }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        ChamsColor.red = ((Number) this.colorRedValue.get()).floatValue() / '\u00FF';
        ChamsColor.green = ((Number) this.colorGreenValue.get()).floatValue() / '\u00FF';
        ChamsColor.blue = ((Number) this.colorBlueValue.get()).floatValue() / '\u00FF';
        ChamsColor.Apl = ((Number) this.colorAValue.get()).floatValue() / '\u00FF';
    }
    @Override
    public String getTag() {
        return modeValue.get();
    }
}