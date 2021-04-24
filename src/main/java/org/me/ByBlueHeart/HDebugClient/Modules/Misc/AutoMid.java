package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;

@ModuleInfo(name = "AutoMid", description = "Auto Mid Click.", category = ModuleCategory.MISC)
public class AutoMid extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.gameSettings.keyBindUseItem.pressed = true;
    }
}
