
package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.features.command.Command
import net.blueheart.hdebug.ui.client.altmanager.GuiAltManager
import net.blueheart.hdebug.utils.ServerUtils
import net.blueheart.hdebug.utils.login.MinecraftAccount

class LoginCommand : Command("login", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size <= 1) {
            chatSyntax("login <username/email> [password]")
            return
        }

        val result: String = if (args.size > 2)
            GuiAltManager.login(MinecraftAccount(args[1], args[2]))
        else
            GuiAltManager.login(MinecraftAccount(args[1]))

        chat(result)

        if (result.startsWith("§cYour name is now")) {
            if (mc.isIntegratedServerRunning)
                return

            mc.theWorld.sendQuittingDisconnectingPacket()
            ServerUtils.connectToLastServer()
        }
    }
}