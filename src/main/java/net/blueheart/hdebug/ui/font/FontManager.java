package net.blueheart.hdebug.ui.font;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.utils.Logger;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.*;

public class FontManager {
    public static UnicodeFontRenderer yahei12;
    public static UnicodeFontRenderer yahei18;
    public static UnicodeFontRenderer yahei20;
    public static UnicodeFontRenderer yahei22;
    public static UnicodeFontRenderer yahei24;

    public static void loadFont() {
        try {
            InputStream inputStream = new FileInputStream(new File(HDebug.fileManager.fontsDir, "Default.ttf"));

            Font font = null;
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);

            Logger.printconsolemessage("Loading Fonts......");
            yahei12 = new UnicodeFontRenderer(font.deriveFont(12F));
            yahei18 = new UnicodeFontRenderer(font.deriveFont(18F));
            yahei20 = new UnicodeFontRenderer(font.deriveFont(20F));
            yahei22 = new UnicodeFontRenderer(font.deriveFont(22F));
            yahei24 = new UnicodeFontRenderer(font.deriveFont(24F));
        } catch (FileNotFoundException e) {
            Logger.printconsolemessage("Default.ttf Font Not Found! Please put Default.ttf to Fonts dir!");
        } catch (FontFormatException e) {
            e.printStackTrace();
            Logger.printconsolemessage("Font Format Error!");
        } catch (IOException e) {
            Logger.printconsolemessage("Font Format Error! Check Your Font File!");
            e.printStackTrace();
        }
        if (Minecraft.getMinecraft().gameSettings.language != null) {
            yahei12.setUnicodeFlag(true);
            yahei18.setUnicodeFlag(true);
            yahei20.setUnicodeFlag(true);
            yahei22.setUnicodeFlag(true);
            yahei24.setUnicodeFlag(true);
            yahei12.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            Logger.printconsolemessage("Loading Default12......");
            yahei18.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            Logger.printconsolemessage("Loading Default18......");
            yahei20.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            Logger.printconsolemessage("Loading Default20......");
            yahei22.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            Logger.printconsolemessage("Loading Default22......");
            yahei24.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            Logger.printconsolemessage("Loading Default24......");
        }

    }
}
