package net.blueheart.hdebug.features.special

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
object AutoReconnect {
    const val MAX = 60000
    const val MIN = 1000

    var isEnabled = true
        private set
    var delay = 5000
        set(value) {
            isEnabled = delay < MAX

            field = value
        }
}