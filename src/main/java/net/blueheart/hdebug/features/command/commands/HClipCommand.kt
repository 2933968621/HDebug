
package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.features.command.Command
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils

class HClipCommand : Command("hclip", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            try {
                MovementUtils.forward(args[1].toDouble())
                chat("You were teleported.")
            } catch (exception: NumberFormatException) {
                chatSyntaxError()
            }
            return
        }

        chatSyntax("hclip <value>")
    }
}