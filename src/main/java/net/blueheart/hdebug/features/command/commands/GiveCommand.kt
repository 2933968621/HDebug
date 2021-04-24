
package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.features.command.Command
import net.blueheart.hdebug.utils.item.ItemUtils
import net.blueheart.hdebug.utils.misc.StringUtils
import net.minecraft.item.Item
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction
import org.me.ByBlueHeart.HDebugClient.Modules.Misc.Give

class GiveCommand : Command("give", arrayOf("item", "i", "get")) {
    /**
     * Execute commands with provided [args]
     */
    var give = HDebug.moduleManager.getModule(Give::class.java) as Give?
    override fun execute(args: Array<String>) {
        if(give?.state == false) {
            if (mc.playerController.isNotCreative) {
                chat("§c§lError: §3You need to be in creative mode.")
                return
            }
        }

        if (args.size > 1) {
            val itemStack = ItemUtils.createItem(StringUtils.toCompleteString(args, 1))

            if (itemStack == null) {
                chatSyntaxError()
                return
            }

            var emptySlot = -1

            for (i in 36..44) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).stack == null) {
                    emptySlot = i
                    break
                }
            }

            if (emptySlot == -1) {
                for (i in 9..44) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).stack == null) {
                        emptySlot = i
                        break
                    }
                }
            }

            if (emptySlot != -1) {
                mc.netHandler.addToSendQueue(C10PacketCreativeInventoryAction(emptySlot, itemStack))
                chat("§7Given [§8${itemStack.displayName}§7] * §8${itemStack.stackSize}§7 to §8${mc.getSession().username}§7.")
            } else
                chat("Your inventory is full.")
            return
        }

        chatSyntax("give <item> [amount] [data] [datatag]")
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        return when (args.size) {
            1 -> {
                return Item.itemRegistry.keys
                    .map { it.resourcePath.toLowerCase() }
                    .filter { it.startsWith(args[0], true) }
            }
            else -> emptyList()
        }
    }
}