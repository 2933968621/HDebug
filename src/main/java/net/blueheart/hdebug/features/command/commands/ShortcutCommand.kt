
package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.features.command.Command
import net.blueheart.hdebug.utils.misc.StringUtils

class ShortcutCommand: Command("shortcut", arrayOf()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        when {
            args.size > 3 && args[1].equals("add", true) -> {
                try {
                    HDebug.commandManager.registerShortcut(args[2],
                            StringUtils.toCompleteString(args, 3))

                    chat("Successfully added shortcut.")
                } catch (e: IllegalArgumentException) {
                    chat(e.message!!)
                }
            }

            args.size >= 3 && args[1].equals("remove", true) -> {
                if (HDebug.commandManager.unregisterShortcut(args[2]))
                    chat("Successfully removed shortcut.")
                else
                    chat("Shortcut does not exist.")
            }

            else -> chat("shortcut <add <shortcut_name> <script>/remove <shortcut_name>>")
        }
    }
}
