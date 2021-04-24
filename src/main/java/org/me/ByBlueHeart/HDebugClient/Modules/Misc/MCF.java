package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.Render2DEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.file.configs.FriendsConfig;
import net.blueheart.hdebug.utils.ClientUtils;
import net.blueheart.hdebug.utils.render.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

@ModuleInfo(name = "MCF", description = "Allows you to add a player as a friend by mid clicking him.", category = ModuleCategory.MISC)
public class MCF extends Module {
    private boolean wasDown;

    @EventTarget
    public void onRender(Render2DEvent event) {
        if(mc.currentScreen != null)
            return;

        if(!wasDown && Mouse.isButtonDown(2)) {
            final Entity entity = mc.objectMouseOver.entityHit;

            if(entity instanceof EntityPlayer) {
                final String playerName = ColorUtils.stripColor(entity.getName());
                final FriendsConfig friendsConfig = HDebug.fileManager.friendsConfig;

                if(!friendsConfig.isFriend(playerName)) {
                    friendsConfig.addFriend(playerName);
                    HDebug.fileManager.saveConfig(friendsConfig);
                    ClientUtils.displayChatMessage("§a§l" + playerName + "§c was added to your friends.");
                }else{
                    friendsConfig.removeFriend(playerName);
                    HDebug.fileManager.saveConfig(friendsConfig);
                    ClientUtils.displayChatMessage("§a§l" + playerName + "§c was removed from your friends.");
                }
            }else
                ClientUtils.displayChatMessage("§c§lError: §aYou need to select a player.");
        }

        wasDown = Mouse.isButtonDown(2);
    }
}
