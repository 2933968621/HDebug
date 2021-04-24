/*
 * Decompiled with CFR 0_132.
 */
package net.blueheart.hdebug.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Helper {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void sendMessageOLD(String msg) {
        Object[] arrobject = new Object[2];
        arrobject[0] = (Object)((Object)EnumChatFormatting.BLUE) + "HDebug" + (Object)((Object)EnumChatFormatting.GRAY) + ": ";
        arrobject[1] = msg;
        Helper.mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", arrobject)));
    }

    public static void sendMessage(String message) {
        new ChatUtils.ChatMessageBuilder(true, true).appendText(message).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static void sendMessageWithoutPrefix(String message) {
        new ChatUtils.ChatMessageBuilder(false, true).appendText(message).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static boolean onServer(String server) {
        if (!mc.isSingleplayer() && Helper.mc.getCurrentServerData().serverIP.toLowerCase().contains(server)) {
            return true;
        }
        return false;
    }
}