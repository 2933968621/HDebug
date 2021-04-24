
package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.features.command.Command
import net.blueheart.hdebug.utils.ClientUtils
import org.lwjgl.input.Keyboard

class BindsCommand : Command("binds", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            if (args[1].equals("clear", true)) {
                for (module in HDebug.moduleManager.modules)
                    module.keyBind = Keyboard.KEY_NONE

                chat("Removed all binds.")
                return
            }
        }

        chat("§c§lBinds")
        HDebug.moduleManager.modules.filter { it.keyBind != Keyboard.KEY_NONE }.forEach {
            ClientUtils.displayChatMessage("§6> §c${it.name}: §a§l${Keyboard.getKeyName(it.keyBind)}")
        }
        chatSyntax("binds clear")
    }
}