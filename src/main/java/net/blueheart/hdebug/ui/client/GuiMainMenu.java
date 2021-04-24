package net.blueheart.hdebug.ui.client;

import java.awt.image.BufferedImage;
import java.io.File;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.ui.client.GuiCredits;
import net.blueheart.hdebug.ui.client.GuiIconButton;
import net.blueheart.hdebug.ui.client.GuiModsMenu;
import net.blueheart.hdebug.ui.client.GuiServerStatus;
import net.blueheart.hdebug.ui.client.altmanager.GuiAltManager;
import net.blueheart.hdebug.ui.font.Fonts;
import net.blueheart.hdebug.utils.misc.MiscUtils;
import net.blueheart.hdebug.utils.render.ColorUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import java.awt.Component;
import javax.swing.JFileChooser;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    public void initGui() {
        final int j = this.height - 80;
        this.buttonList.add(new GuiIconButton(1, this.width / 2 - 15 - 240, j + 24, 30, 30, "Singleplayer", new ResourceLocation("hdebug/singleplayer.png")));
        this.buttonList.add(new GuiIconButton(2, this.width / 2 - 15 - 180, j + 24, 30, 30, "Multiplayer", new ResourceLocation("hdebug/multiplayer.png")));
        this.buttonList.add(new GuiIconButton(100, this.width / 2 - 15 - 120, j + 24, 30, 30, "AltManager", new ResourceLocation("hdebug/altmanager.png")));
        this.buttonList.add(new GuiIconButton(101, this.width / 2 - 15 - 60, j + 24, 30, 30, "Server Status", new ResourceLocation("hdebug/server_status.png")));
        this.buttonList.add(new GuiIconButton(103, this.width / 2 - 15, j + 24, 30, 30, "Mods", new ResourceLocation("hdebug/mods.png")));
        this.buttonList.add(new GuiIconButton(102, this.width / 2 - 15 + 60, j + 24, 30, 30, "Change Wallpaper", new ResourceLocation("hdebug/change_wallpaper.png")));
        this.buttonList.add(new GuiIconButton(108, this.width / 2 - 15 + 120, j + 24, 30, 30, "Credits", new ResourceLocation("hdebug/credits.png")));
        this.buttonList.add(new GuiIconButton(0, this.width / 2 - 15 + 180, j + 24, 30, 30, "Options", new ResourceLocation("hdebug/options.png")));
        this.buttonList.add(new GuiIconButton(4, this.width / 2 - 15 + 240, j + 24, 30, 30, "Quit", new ResourceLocation("hdebug/exit.png")));
        super.initGui();
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        Fonts.fontBold180.drawCenteredString(HDebug.CLIENT_NAME, (int)(this.width / 2.0f), (int)(this.height / 4.0f + 50.0f), Color.WHITE.getRGB(), true);
        Fonts.font35.drawCenteredString("b" + HDebug.CLIENT_VERSION, (int)(this.width / 2.0f + 100.0f), (int)(this.height / 4.0f + 50.0f + Fonts.font35.FONT_HEIGHT - 10.0f), 16777215, true);
        Gui.drawRect(0, this.height - 70, this.width, this.height, new Color(30, 30, 30).getRGB());
        for (int i = 0; i < this.width; ++i) {
            Gui.drawRect(i, this.height - 75, i + 1, this.height - 70, ColorUtils.rainbow(4000000L * i).getRGB());
        }
        this.drawString(Fonts.font40, "Made by " + HDebug.CLIENT_CREATOR, 2, this.height - 10, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 4: {
                this.mc.shutdown();
                break;
            }
            case 100: {
                this.mc.displayGuiScreen(new GuiAltManager(this));
                break;
            }
            case 101: {
                this.mc.displayGuiScreen(new GuiServerStatus(this));
                break;
            }
            case 102: {
                if (Keyboard.isKeyDown(42)) {
                    HDebug.INSTANCE.setBackground(null);
                    HDebug.fileManager.backgroundFile.delete();
                    return;
                }
                final JFileChooser jFileChooser = new JFileChooser() {
                    @Override
                    protected JDialog createDialog(final Component parent) throws HeadlessException {
                        final JDialog jDialog = super.createDialog(parent);
                        jDialog.setAlwaysOnTop(true);
                        return jDialog;
                    }
                };
                jFileChooser.setFileSelectionMode(0);
                final int returnValue = jFileChooser.showOpenDialog(null);
                if (returnValue != 0) {
                    break;
                }
                final File selectedFile = jFileChooser.getSelectedFile();
                if (selectedFile.isDirectory()) {
                    return;
                }
                try {
                    Files.copy(selectedFile.toPath(), new FileOutputStream(HDebug.fileManager.backgroundFile));
                    final BufferedImage image = ImageIO.read(new FileInputStream(HDebug.fileManager.backgroundFile));
                    HDebug.INSTANCE.setBackground(new ResourceLocation("hdebug/background.png"));
                    this.mc.getTextureManager().loadTexture(HDebug.INSTANCE.getBackground(), new DynamicTexture(image));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                    HDebug.fileManager.backgroundFile.delete();
                }
                break;
            }
            case 103: {
                this.mc.displayGuiScreen(new GuiModsMenu(this));
                break;
            }
            case 108: {
                this.mc.displayGuiScreen(new GuiCredits(this));
                break;
            }
        }
    }

    protected void keyTyped(final char typedChar, final int keyCode) {
    }
}
