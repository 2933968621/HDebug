package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.features.command.Command
import net.blueheart.hdebug.ui.client.clickgui.ClickGui
import net.blueheart.hdebug.ui.font.Fonts
import net.blueheart.hdebug.features.command.CommandManager

class ReloadCommand : Command("reload", arrayOf("configreload")) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        chat("Reloading...")
        chat("§c§lReloading commands...")
        HDebug.commandManager = CommandManager()
        HDebug.commandManager.registerCommands()
        chat("§c§lReloading scripts...")
        HDebug.scriptManager.reloadScripts()
        chat("§c§lReloading fonts...")
        Fonts.loadFonts()
        chat("§c§lReloading modules...")
        HDebug.fileManager.loadConfig(HDebug.fileManager.modulesConfig)
        chat("§c§lReloading values...")
        HDebug.fileManager.loadConfig(HDebug.fileManager.valuesConfig)
        chat("§c§lReloading accounts...")
        HDebug.fileManager.loadConfig(HDebug.fileManager.accountsConfig)
        chat("§c§lReloading friends...")
        HDebug.fileManager.loadConfig(HDebug.fileManager.friendsConfig)
        chat("§c§lReloading xray...")
        HDebug.fileManager.loadConfig(HDebug.fileManager.xrayConfig)
        chat("§c§lReloading HUD...")
        HDebug.fileManager.loadConfig(HDebug.fileManager.hudConfig)
        chat("§c§lReloading ClickGUI...")
        HDebug.clickGui = ClickGui()
        HDebug.fileManager.loadConfig(HDebug.fileManager.clickGuiConfig)
        chat("Reloaded.")
    }
}