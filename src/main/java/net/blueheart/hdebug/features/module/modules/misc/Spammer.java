package net.blueheart.hdebug.features.module.modules.misc;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.misc.RandomUtils;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.blueheart.hdebug.utils.timer.TimeUtils;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.IntegerValue;
import net.blueheart.hdebug.value.TextValue;
import org.lwjgl.input.Keyboard;

import java.util.Random;

@ModuleInfo(name = "Spammer", description = "Spams the chat with a given message.", category = ModuleCategory.MISC, keyBind = Keyboard.KEY_K)
public class Spammer extends Module {
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

    private final TextValue messageValue = new TextValue("Message", "HDebug Client By BlueHeart QQGroup:1128533970 Discord:https://discord.gg/VErtkWM Url:https://cpnh8n.coding-pages.com/HDebug.html");
    private final BoolValue customValue = new BoolValue("Custom", false);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);
    private final BoolValue HYTValue = new BoolValue("HuaYuTing", true);

    private final MSTimer msTimer = new MSTimer();
    private long delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(msTimer.hasTimePassed(delay)) {
            mc.thePlayer.sendChatMessage(customValue.get() ? replace((HYTValue.get() ? "@" : "") + (PrefixValue.get() ? "[HDebug]" : "") + messageValue.get()) : (HYTValue.get() ? "@" : "") + (PrefixValue.get() ? "[HDebug]" : "") + messageValue.get() + " [" + RandomUtils.randomString(5 + new Random().nextInt(5)) + "]");
            msTimer.reset();
            delay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
        }
    }

    private String replace(String object) {
        final Random r = new Random();

        while(object.contains("%f"))
            object = object.substring(0, object.indexOf("%f")) + r.nextFloat() + object.substring(object.indexOf("%f") + "%f".length());

        while(object.contains("%i"))
            object = object.substring(0, object.indexOf("%i")) + r.nextInt(10000) + object.substring(object.indexOf("%i") + "%i".length());

        while(object.contains("%s"))
            object = object.substring(0, object.indexOf("%s")) + RandomUtils.randomString(r.nextInt(8) + 1) + object.substring(object.indexOf("%s") + "%s".length());

        while(object.contains("%ss"))
            object = object.substring(0, object.indexOf("%ss")) + RandomUtils.randomString(r.nextInt(4) + 1) + object.substring(object.indexOf("%ss") + "%ss".length());

        while(object.contains("%ls"))
            object = object.substring(0, object.indexOf("%ls")) + RandomUtils.randomString(r.nextInt(15) + 1) + object.substring(object.indexOf("%ls") + "%ls".length());
        return object;
    }
}
