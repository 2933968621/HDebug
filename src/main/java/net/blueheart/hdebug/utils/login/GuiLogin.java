package net.blueheart.hdebug.utils.login;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.ScreenEvent;
import net.blueheart.hdebug.ui.client.GuiMainMenu;
import net.blueheart.hdebug.ui.font.FontManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.me.ByBlueHeart.HDebugClient.Utils.HWIDUtil;
import org.me.ByBlueHeart.HDebugClient.Utils.WebUtil;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class GuiLogin extends GuiScreen implements GuiYesNoCallback {
    private Timer timer = new Timer();
    public String text = "IN CHECK YOUR HWID";
    GuiMainMenu mainmenu;
    public GuiLogin() {
        mainmenu = new GuiMainMenu();
    }
    @Override
    public void drawScreen(int x, int y, float z) {
        drawDefaultBackground();
        FontManager.yahei24.drawCenteredString(text, (int) (width / 2F), (int) (height / 2F - 6F), 0x00ffff);
        this.timer.schedule(new TimerTask() {
            public void run() {
                try {
                    if (WebUtil.get("https://cpnh8n.coding-pages.com/HOnlineHWIDList.json").contains(HWIDUtil.getHWID())) {
                        text = "WELCOME YOU BACK!";
                        //if(mc.thePlayer != null) {
                        timer.schedule(new TimerTask() {
                            public void run() {
                                HDebug.eventManager.callEvent(new ScreenEvent(mainmenu));
                            }
                        },  2000);
                        //}
                    } else {
                        StringSelection stsel = new StringSelection(HWIDUtil.getHWID());
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stsel, stsel);
                        text = "HWID CHECK ERROR!YOUR HWID HAS BEEN COPY";
                        timer.schedule(new TimerTask() {
                            public void run() {
                                System.exit(0);
                            }
                        },  5000);
                        return;
                    }
                } catch (NoSuchAlgorithmException | IOException e) {
                    text = "NETWORK ERROR!";
                    timer.schedule(new TimerTask() {
                        public void run() {
                            System.exit(0);
                        }
                    },  5000);
                }
            }
        },  3000);
        super.drawScreen(x, y, z);
    }
}