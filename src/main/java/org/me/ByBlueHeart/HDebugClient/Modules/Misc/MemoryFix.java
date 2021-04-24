package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;

@ModuleInfo(name = "MemoryFix", description = "Memory Fix.", category = ModuleCategory.MISC)
public class MemoryFix extends Module {
    @Override
    public void onEnable() {
        Runtime.getRuntime().gc();
        super.onEnable();
    }
}