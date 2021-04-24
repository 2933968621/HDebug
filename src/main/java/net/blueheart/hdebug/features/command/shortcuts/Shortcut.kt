
package net.blueheart.hdebug.features.command.shortcuts

import net.blueheart.hdebug.features.command.Command

class Shortcut(val name: String, val script: List<Pair<Command, Array<String>>>): Command(name, arrayOf()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        script.forEach { it.first.execute(it.second) }
    }
}
