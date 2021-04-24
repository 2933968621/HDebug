package org.me.ByBlueHeart.HDebugClient.Modules.Render;

import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.FloatValue;
import net.blueheart.hdebug.value.ListValue;

@ModuleInfo(name = "Animations", description = "Animation for blocking.", category = ModuleCategory.RENDER)
public class Animation extends Module {
    public static final ListValue AnimationModeValue = new ListValue("Mode", new String[] {"Sigma", "Jello", "NoSwing", "Slide", "Push", "ExhibitionSwang", "ExhibitionSwank", "ExhibitionSwong", "Jigsaw", "1.7", "Luna", "360°", "HDebug", "ETB", "Avatar", "IDBUG", "NoSword", "Normal", "Custom","Rotate"}, "HDebug");
    public static final FloatValue CustomValue = new FloatValue("CustomX",0F,-10F,10F);
    public static final FloatValue CustomValue2 = new FloatValue("CustomY",0F,-10F,10F);
    public static FloatValue X = new FloatValue("EveryThingBlockX", 0, -1, 0);
    public static FloatValue Y = new FloatValue("EveryThingBlockY", 0, -1, 0);
    public static FloatValue Z = new FloatValue("EveryThingBlockZ", 0, -1, 0);
    public static BoolValue EveryThingBlock = new BoolValue("EveryThingBlock",false);
    public static final FloatValue Speed = new FloatValue("RotateSpeed",10F,1F,50F);
    public String getTag() {
        return AnimationModeValue.get();
    }
}