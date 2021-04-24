package net.blueheart.hdebug

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.blueheart.hdebug.cape.CapeAPI.registerCapeService
import net.blueheart.hdebug.discord.ClientRichPresence
import net.blueheart.hdebug.event.ClientShutdownEvent
import net.blueheart.hdebug.event.EventManager
import net.blueheart.hdebug.features.command.CommandManager
import net.blueheart.hdebug.features.module.ModuleManager
import net.blueheart.hdebug.features.special.AntiForge
import net.blueheart.hdebug.features.special.BungeeCordSpoof
import net.blueheart.hdebug.features.special.DonatorCape
import net.blueheart.hdebug.file.FileManager
import net.blueheart.hdebug.script.ScriptManager
import net.blueheart.hdebug.script.remapper.Remapper.loadSrg
import net.blueheart.hdebug.tabs.BlocksTab
import net.blueheart.hdebug.tabs.ExploitsTab
import net.blueheart.hdebug.tabs.HeadsTab
import net.blueheart.hdebug.ui.client.altmanager.GuiAltManager
import net.blueheart.hdebug.ui.client.clickgui.ClickGui
import net.blueheart.hdebug.ui.client.hud.HUD
import net.blueheart.hdebug.ui.client.hud.HUD.Companion.createDefault
import net.blueheart.hdebug.ui.font.FontManager
import net.blueheart.hdebug.ui.font.Fonts
import net.blueheart.hdebug.utils.ClassUtils.hasForge
import net.blueheart.hdebug.utils.ClientUtils
import net.blueheart.hdebug.utils.InventoryUtils
import net.blueheart.hdebug.utils.Logger
import net.blueheart.hdebug.utils.RotationUtils
import net.blueheart.hdebug.utils.misc.HttpUtils
import net.minecraft.util.ResourceLocation
import org.me.ByBlueHeart.HDebugClient.HDebugMain
import org.me.ByBlueHeart.HDebugClient.Utils.WebUtil
import java.awt.HeadlessException
import java.io.IOException
import javax.swing.JOptionPane

object HDebug {
    lateinit var main: HDebugMain;
    // Client information
    const val CLIENT_NAME = "HDebug"
    const val CLIENT_VERSION = 20
    const val CLIENT_DEVELOPER = false
    const val CLIENT_CREATOR = "CCBlueX / TimeLine"
    const val MINECRAFT_VERSION = "1.8.9"
    const val CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce"

    var isStarting = false

    // Managers
    lateinit var logger: Logger
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    // HUD & ClickGUI
    lateinit var hud: HUD

    lateinit var clickGui: ClickGui

    // Update information
    var latestVersion = 0

    // Menu Background
    var background: ResourceLocation? = null

    // Discord RPC
    private lateinit var clientRichPresence: ClientRichPresence

    /**
     * Execute if client will be started
     */
    fun startClient() {
        isStarting = true

        ClientUtils.getLogger().info("Starting $CLIENT_NAME b$CLIENT_VERSION, by $CLIENT_CREATOR")

        main = HDebugMain(this)
        main.onStarting()

        logger = Logger()
        // Create file manager
        fileManager = FileManager()

        // Crate event manager
        eventManager = EventManager()

        // Register listeners
        eventManager.registerListener(RotationUtils())
        eventManager.registerListener(AntiForge())
        eventManager.registerListener(BungeeCordSpoof())
        eventManager.registerListener(DonatorCape())
        eventManager.registerListener(InventoryUtils())

        // Create command manager
        commandManager = CommandManager()
        //Crate Chinese FontManager
        FontManager.loadFont()
        // Load client fonts
        Fonts.loadFonts()

        // Setup module manager and register modules
        moduleManager = ModuleManager()
        moduleManager.registerModules()

        // Remapper
        try {
            loadSrg()

            // ScriptManager
            scriptManager = ScriptManager()
            scriptManager.loadScripts()
            scriptManager.enableScripts()
        } catch (throwable: Throwable) {
            ClientUtils.getLogger().error("Failed to load scripts.", throwable)
        }

        // Register commands
        commandManager.registerCommands()

        // Load configs
        fileManager.loadConfigs(fileManager.modulesConfig, fileManager.valuesConfig, fileManager.accountsConfig,
                fileManager.friendsConfig, fileManager.xrayConfig, fileManager.shortcutsConfig)

        // ClickGUI
        clickGui = ClickGui()
        fileManager.loadConfig(fileManager.clickGuiConfig)

        // Tabs (Only for Forge!)
        if (hasForge()) {
            BlocksTab()
            ExploitsTab()
            HeadsTab()
        }

        // Register capes service
        try {
            registerCapeService()
        } catch (throwable: Throwable) {
            ClientUtils.getLogger().error("Failed to register cape service", throwable)
        }

        try {
            if (WebUtil.get("https://coding-pages-bucket-495056-1397696-5160-376218-1303446795.cos-website.ap-hongkong.myqcloud.com/HDebugUse.json").contains("true")) {
                HDebugMain.nice = true
            } else {
                //StringSelection stsel = new StringSelection(HWIDUtil.getHWID());
                //Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stsel, stsel);
                //JOptionPane.showMessageDialog(null, "你没有通过HWID验证！您的HWID：" + HWIDUtil.getHWID() + " 已自动复制到剪辑板中", "你没有通过HDebug的HWID验证！", JOptionPane.ERROR_MESSAGE);
                HDebugMain.nice = false
                System.exit(0)
                return
            }
        } catch (e: HeadlessException) {
            // TODO 自动生成的 catch 块
            HDebugMain.nice = false
            JOptionPane.showMessageDialog(null, "警告 无法连接至服务器", "服务器连接错误", JOptionPane.ERROR_MESSAGE)
            System.exit(0)
            e.printStackTrace()
        } catch (e: IOException) {
            HDebugMain.nice = false
            JOptionPane.showMessageDialog(null, "警告 无法连接至服务器", "服务器连接错误", JOptionPane.ERROR_MESSAGE)
            System.exit(0)
            e.printStackTrace()
        }

        // Setup Discord RPC
        try {
            clientRichPresence = ClientRichPresence()
            clientRichPresence.setup()
        } catch (throwable: Throwable) {
            ClientUtils.getLogger().error("Failed to setup Discord RPC.", throwable)
        }

        // Set HUD
        hud = createDefault()
        fileManager.loadConfig(fileManager.hudConfig)

        // Disable optifine fastrender
        ClientUtils.disableFastRender()

        try {
            // Read versions json from cloud
            val jsonObj = JsonParser()
                    .parse(HttpUtils.get("$CLIENT_CLOUD/versions.json"))

            // Check json is valid object and has current minecraft version
            if (jsonObj is JsonObject && jsonObj.has(MINECRAFT_VERSION)) {
                // Get offical latest client version
                latestVersion = jsonObj[MINECRAFT_VERSION].asInt
            }
        } catch (exception: Throwable) { // Print throwable to console
            ClientUtils.getLogger().error("Failed to check for updates.", exception)
        }

        // Load generators
        GuiAltManager.loadGenerators()

        main.onStarted()

        // Set is starting status
        isStarting = false
    }

    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        // Call client shutdown
        eventManager.callEvent(ClientShutdownEvent())

        // Save all available configs
        fileManager.saveAllConfigs()

        // Shutdown discord rpc
        clientRichPresence.shutdown()
    }

}