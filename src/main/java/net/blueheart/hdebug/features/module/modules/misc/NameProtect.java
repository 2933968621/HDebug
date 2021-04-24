package net.blueheart.hdebug.features.module.modules.misc;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.TextEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.file.configs.FriendsConfig;
import net.blueheart.hdebug.utils.misc.StringUtils;
import net.blueheart.hdebug.utils.render.ColorUtils;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.TextValue;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModuleInfo(name = "FakeName", description = "Changes playernames clientside.", category = ModuleCategory.MISC)
public class NameProtect extends Module {
    private final TextValue fakeNameValue = new TextValue("FakeName", "§4Chara");
    public final BoolValue allPlayersValue = new BoolValue("AllPlayers", false);
    public final BoolValue skinProtectValue = new BoolValue("SkinProtect", true);

    @EventTarget(ignoreCondition = true)
    public void onText(final TextEvent event) {
        if(mc.thePlayer == null || event.getText().contains("§8[§9§lHDebug§8] §3"))
            return;

        for (final FriendsConfig.Friend friend : HDebug.fileManager.friendsConfig.getFriends())
            event.setText(StringUtils.replace(event.getText(), friend.getPlayerName(), ColorUtils.translateAlternateColorCodes(friend.getAlias()) + "§f"));

        if(!getState())
            return;

        event.setText(StringUtils.replace(event.getText(), mc.thePlayer.getName(), ColorUtils.translateAlternateColorCodes(fakeNameValue.get()) + "§f"));

        if(allPlayersValue.get())
            for(final NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap())
                event.setText(StringUtils.replace(event.getText(), playerInfo.getGameProfile().getName(), "HDebug Name Protect"));
    }
}
