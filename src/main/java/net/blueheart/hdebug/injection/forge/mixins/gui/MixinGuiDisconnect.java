package net.blueheart.hdebug.injection.forge.mixins.gui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.SessionEvent;
import net.blueheart.hdebug.features.special.AntiForge;
import net.blueheart.hdebug.ui.client.altmanager.GuiAltManager;
import net.blueheart.hdebug.utils.ClientUtils;
import net.blueheart.hdebug.utils.ServerUtils;
import net.blueheart.hdebug.utils.login.LoginUtils;
import net.blueheart.hdebug.utils.login.MinecraftAccount;
import net.blueheart.hdebug.utils.misc.RandomUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;
import java.util.List;
import java.util.Random;

@Mixin(GuiDisconnected.class)
public abstract class MixinGuiDisconnect extends MixinGuiScreen {

    @Shadow
    private int field_175353_i;

    private GuiButton reconnectButton;
    private GuiButton forgeBypassButton;
    private int reconnectTimer;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        reconnectTimer = 0;
        buttonList.add(reconnectButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 22, "Reconnect"));
        buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 44, 98, 20, "New TheAltening alt"));
        buttonList.add(new GuiButton(4, this.width / 2 + 2, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 44, 98, 20, "Random username"));
        buttonList.add(forgeBypassButton = new GuiButton(2, this.width / 2 - 100, this.height / 2 + field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 66, "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off")));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        switch (button.id) {
            case 1:
                ServerUtils.connectToLastServer();
                break;
            case 2:
                AntiForge.enabled = !AntiForge.enabled;
                forgeBypassButton.displayString = "Bypass AntiForge: " + (AntiForge.enabled ? "On" : "Off");
                HDebug.fileManager.saveConfig(HDebug.fileManager.valuesConfig);
                break;
            case 3:
                final List<MinecraftAccount> accounts = HDebug.fileManager.accountsConfig.altManagerMinecraftAccounts;
                if (accounts.isEmpty()) break;

                final MinecraftAccount minecraftAccount = accounts.get(new Random().nextInt(accounts.size()));
                GuiAltManager.login(minecraftAccount);
                ServerUtils.connectToLastServer();
                break;
            case 4:
                LoginUtils.loginCracked(RandomUtils.randomString(RandomUtils.nextInt(5, 16)));
                ServerUtils.connectToLastServer();
                break;
        }
    }

    @Override
    public void updateScreen() {
        reconnectTimer++;
        if (reconnectTimer > 100)
            ServerUtils.connectToLastServer();
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void drawScreen(CallbackInfo callbackInfo) {
        reconnectButton.displayString = "Reconnect (" + (5 - reconnectTimer / 20) + ")";
    }
}