
package net.blueheart.hdebug.features.command.commands

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.Listenable
import net.blueheart.hdebug.event.Render2DEvent
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.command.Command
import net.blueheart.hdebug.utils.ClientUtils
import net.blueheart.hdebug.utils.render.RenderUtils
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation

class TacoCommand : Command("taco", emptyArray()), Listenable {
    private var toggle = false
    private var image = 0
    private var running = 0f
    private val tacoTextures = arrayOf(
            ResourceLocation("liquidbounce/taco/1.png"),
            ResourceLocation("liquidbounce/taco/2.png"),
            ResourceLocation("liquidbounce/taco/3.png"),
            ResourceLocation("liquidbounce/taco/4.png"),
            ResourceLocation("liquidbounce/taco/5.png"),
            ResourceLocation("liquidbounce/taco/6.png"),
            ResourceLocation("liquidbounce/taco/7.png"),
            ResourceLocation("liquidbounce/taco/8.png"),
            ResourceLocation("liquidbounce/taco/9.png"),
            ResourceLocation("liquidbounce/taco/10.png"),
            ResourceLocation("liquidbounce/taco/11.png"),
            ResourceLocation("liquidbounce/taco/12.png")
    )

    init {
        HDebug.eventManager.registerListener(this)
    }

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        toggle = !toggle
        ClientUtils.displayChatMessage(if (toggle) "§aTACO TACO TACO. :)" else "§cYou made the little taco sad! :(")
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (!toggle)
            return

        running += 0.15f * RenderUtils.deltaTime
        val scaledResolution = ScaledResolution(mc)
        RenderUtils.drawImage(tacoTextures[image], running.toInt(), scaledResolution.scaledHeight - 60, 64, 32)
        if (scaledResolution.scaledWidth <= running)
            running = -64f
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!toggle) {
            image = 0
            return
        }

        image++
        if (image >= tacoTextures.size) image = 0
    }

    override fun handleEvents() = true

    override fun tabComplete(args: Array<String>): List<String> {
        return listOf("TACO")
    }
}