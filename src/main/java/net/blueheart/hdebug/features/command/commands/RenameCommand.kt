
package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.features.command.Command
import net.blueheart.hdebug.utils.misc.StringUtils
import net.blueheart.hdebug.utils.render.ColorUtils

class RenameCommand : Command("rename", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            if (mc.playerController.isNotCreative) {
                chat("§c§lError: §3You need to be in creative mode.")
                return
            }

            val item = mc.thePlayer.heldItem
            if (item == null || item.item == null) {
                chat("§c§lError: §3You need to hold a item.")
                return
            }

            item.setStackDisplayName(ColorUtils.translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)))
            chat("§3Item renamed to '${item.displayName}§3'")
            return
        }

        chatSyntax("rename <name>")
    }
}