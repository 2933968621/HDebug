package net.blueheart.hdebug.ui.client;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.features.special.AntiForge;
import net.blueheart.hdebug.ui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiAntiForge extends GuiScreen {

    private final GuiScreen prevGui;

    private GuiButton enabledButton;
    private GuiButton fmlButton;
    private GuiButton proxyButton;
    private GuiButton payloadButton;

    public GuiAntiForge(final GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        buttonList.add(enabledButton = new GuiButton(1, width / 2 - 100, height / 4 + 35, "Enabled (" + (AntiForge.enabled ? "On" : "Off") + ")"));
        buttonList.add(fmlButton = new GuiButton(2, width / 2 - 100, height / 4 + 50 + 25, "Block FML (" + (AntiForge.blockFML ? "On" : "Off") + ")"));
        buttonList.add(proxyButton = new GuiButton(3, width / 2 - 100, height / 4 + 50 + 25 * 2, "Block FML Proxy Packet (" + (AntiForge.blockProxyPacket ? "On" : "Off") + ")"));
        buttonList.add(payloadButton = new GuiButton(4, width / 2 - 100, height / 4 + 50 + 25 * 3, "Block Payload Packets (" + (AntiForge.blockPayloadPackets ? "On" : "Off") + ")"));

        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 55 + 25 * 4 + 5, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch(button.id) {
            case 1:
                AntiForge.enabled = !AntiForge.enabled;
                enabledButton.displayString = "Enabled (" + (AntiForge.enabled ? "On" : "Off") + ")";
                HDebug.fileManager.saveConfig(HDebug.fileManager.valuesConfig);
                break;
            case 2:
                AntiForge.blockFML = !AntiForge.blockFML;
                fmlButton.displayString = "Block FML (" + (AntiForge.blockFML ? "On" : "Off") + ")";
                HDebug.fileManager.saveConfig(HDebug.fileManager.valuesConfig);
                break;
            case 3:
                AntiForge.blockProxyPacket = !AntiForge.blockProxyPacket;
                proxyButton.displayString = "Block FML Proxy Packet (" + (AntiForge.blockProxyPacket ? "On" : "Off") + ")";
                HDebug.fileManager.saveConfig(HDebug.fileManager.valuesConfig);
                break;
            case 4:
                AntiForge.blockPayloadPackets = !AntiForge.blockPayloadPackets;
                payloadButton.displayString = "Block Payload Packets (" + (AntiForge.blockPayloadPackets ? "On" : "Off") + ")";
                HDebug.fileManager.saveConfig(HDebug.fileManager.valuesConfig);
                break;
            case 0:
                mc.displayGuiScreen(prevGui);
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        Fonts.fontBold180.drawCenteredString("AntiForge", (int) (width / 2F), (int) (height / 8F + 5F), 0x00ffff, true);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(Keyboard.KEY_ESCAPE == keyCode) {
            mc.displayGuiScreen(prevGui);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }
}