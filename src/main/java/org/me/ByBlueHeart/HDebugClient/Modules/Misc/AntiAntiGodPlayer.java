package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.blueheart.hdebug.utils.timer.TimeUtils;
import net.blueheart.hdebug.value.IntegerValue;

@ModuleInfo(name = "AntiAntiGodPlayer", description = "Anti AntiGodPlayer Js.", category = ModuleCategory.MISC)
public class AntiAntiGodPlayer extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 5000) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int minDelayValueObject = minDelayValue.get();

            if(minDelayValueObject > newValue)
                set(minDelayValueObject);
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
        }
    };

    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 500, 0, 5000) {

        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int maxDelayValueObject = maxDelayValue.get();

            if(maxDelayValueObject < newValue)
                set(maxDelayValueObject);
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
        }
    };

    private final MSTimer msTimer = new MSTimer();
    private long delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(msTimer.hasTimePassed(delay)) {
            mc.thePlayer.sendChatMessage("@起床战争 >> *你*[?20] (你母的队伍) 杀死了 " + mc.thePlayer.getGameProfile().getName() + "(你母的队伍)");
            msTimer.reset();
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
        }
    }
}