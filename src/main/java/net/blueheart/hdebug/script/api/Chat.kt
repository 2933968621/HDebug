/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.blueheart.hdebug.script.api

import net.blueheart.hdebug.utils.ClientUtils

/**
 * A script api class for chat better chat support
 *
 * @author CCBlueX
 */
object Chat {

    /**
     * Print a message to chat
     *
     * @param message A string as message for printing to chat
     */
    @JvmStatic
    fun print(message : String) {
        ClientUtils.displayChatMessage(message)
    }
}