package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.features.command.Command
import net.blueheart.hdebug.ui.client.clickgui.ClickGui
import net.blueheart.hdebug.utils.ClientUtils
import net.blueheart.hdebug.utils.misc.MiscUtils
import org.apache.commons.io.IOUtils
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.zip.ZipFile

class ScriptManagerCommand : Command("scriptmanager", arrayOf("scripts")) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            when {
                args[1].equals("import", true) -> {
                    try {
                        val file = MiscUtils.openFileChooser() ?: return
                        val fileName = file.name

                        if (fileName.endsWith(".js")) {
                            HDebug.scriptManager.importScript(file)

                            HDebug.clickGui = ClickGui()
                            HDebug.fileManager.loadConfig(HDebug.fileManager.clickGuiConfig)

                            chat("Successfully imported script.")
                            return
                        } else if (fileName.endsWith(".zip")) {
                            val zipFile = ZipFile(file)
                            val entries = zipFile.entries()
                            val scriptFiles = ArrayList<File>()

                            while (entries.hasMoreElements()) {
                                val entry = entries.nextElement()
                                val entryName = entry.name
                                val entryFile = File(HDebug.scriptManager.scriptsFolder, entryName)

                                if (entry.isDirectory) {
                                    entryFile.mkdir()
                                    continue
                                }

                                val fileStream = zipFile.getInputStream(entry)
                                val fileOutputStream = FileOutputStream(entryFile)

                                IOUtils.copy(fileStream, fileOutputStream)
                                fileOutputStream.close()
                                fileStream.close()

                                if (!entryName.contains("/"))
                                    scriptFiles.add(entryFile)
                            }

                            scriptFiles.forEach { scriptFile -> HDebug.scriptManager.loadScript(scriptFile) }

                            HDebug.clickGui = ClickGui()
                            HDebug.fileManager.loadConfig(HDebug.fileManager.clickGuiConfig)
                            HDebug.fileManager.loadConfig(HDebug.fileManager.hudConfig)

                            chat("Successfully imported script.")
                            return
                        }

                        chat("The file extension has to be .js or .zip")
                    } catch (t: Throwable) {
                        ClientUtils.getLogger().error("Something went wrong while importing a script.", t)
                        chat("${t.javaClass.name}: ${t.message}")
                    }
                }

                args[1].equals("delete", true) -> {
                    try {
                        if (args.size <= 2) {
                            chatSyntax("scriptmanager delete <index>")
                            return
                        }

                        val scriptIndex = args[2].toInt()
                        val scripts = HDebug.scriptManager.scripts

                        if (scriptIndex >= scripts.size) {
                            chat("Index $scriptIndex is too high.")
                            return
                        }

                        val script = scripts[scriptIndex]

                        HDebug.scriptManager.deleteScript(script)

                        HDebug.clickGui = ClickGui()
                        HDebug.fileManager.loadConfig(HDebug.fileManager.clickGuiConfig)
                        HDebug.fileManager.loadConfig(HDebug.fileManager.hudConfig)
                        chat("Successfully deleted script.")
                    } catch (numberFormat: NumberFormatException) {
                        chatSyntaxError()
                    } catch (t: Throwable) {
                        ClientUtils.getLogger().error("Something went wrong while deleting a script.", t)
                        chat("${t.javaClass.name}: ${t.message}")
                    }
                }

                args[1].equals("reload", true) -> {
                    try {
                        HDebug.scriptManager.reloadScripts()
                        chat("Successfully reloaded all scripts.")
                    } catch (t: Throwable) {
                        ClientUtils.getLogger().error("Something went wrong while reloading all scripts.", t)
                        chat("${t.javaClass.name}: ${t.message}")
                    }
                }

                args[1].equals("folder", true) -> {
                    try {
                        Desktop.getDesktop().open(HDebug.scriptManager.scriptsFolder)
                        chat("Successfully opened scripts folder.")
                    } catch (t: Throwable) {
                        ClientUtils.getLogger().error("Something went wrong while trying to open your scripts folder.", t)
                        chat("${t.javaClass.name}: ${t.message}")
                    }
                }
            }

            return
        }

        val scriptManager = HDebug.scriptManager

        if (scriptManager.scripts.isNotEmpty()) {
            chat("§c§lScripts")
            scriptManager.scripts.forEachIndexed { index, script -> chat("$index: §a§l${script.scriptName} §a§lv${script.scriptVersion} §3by §a§l${script.scriptAuthor}") }
        }

        chatSyntax("scriptmanager <import/delete/reload/folder>")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return when (args.size) {
            1 -> listOf("delete", "import", "folder", "reload")
                    .filter { it.startsWith(args[0], true) }
            else -> emptyList()
        }
    }
}