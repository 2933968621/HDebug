package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.Logger;
import org.me.ByBlueHeart.HDebugClient.Utils.HWIDUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@ModuleInfo(name = "ShowHWID", description = "Show Your HWID In Chat.", category = ModuleCategory.MISC)
public class ShowHWID extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent event) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Logger.printinfo("你的HWID:" + HWIDUtil.getHWID());
        toggle();
    }
}