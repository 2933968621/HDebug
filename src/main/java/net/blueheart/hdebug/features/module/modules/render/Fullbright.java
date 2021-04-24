package net.blueheart.hdebug.features.module.modules.render;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.ClientShutdownEvent;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(name = "FullBright", description = "Brightens up the world around you.", category = ModuleCategory.RENDER)
public class Fullbright extends Module {

    private final ListValue modeValue = new ListValue("Mode", new String[] {"Gamma", "NightVision"}, "Gamma");

    private float prevGamma = -1;

    @Override
    public void onEnable() {
        prevGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        if(prevGamma == -1)
            return;

        mc.gameSettings.gammaSetting = prevGamma;
        prevGamma = -1;
        if(mc.thePlayer != null) mc.thePlayer.removePotionEffectClient(Potion.nightVision.id);
    }

    @EventTarget(ignoreCondition = true)
    public void onUpdate(final UpdateEvent event) {
        if (getState() || HDebug.moduleManager.getModule(XRay.class).getState()) {
            switch(modeValue.get().toLowerCase()) {
                case "gamma":
                    if(mc.gameSettings.gammaSetting <= 100F)
                        mc.gameSettings.gammaSetting++;
                    break;
                case "nightvision":
                    mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1337, 1));
                    break;
            }
        }else if(prevGamma != -1) {
            mc.gameSettings.gammaSetting = prevGamma;
            prevGamma = -1;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onShutdown(final ClientShutdownEvent event) {
        onDisable();
    }
}
