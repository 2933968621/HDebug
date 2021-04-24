package net.blueheart.hdebug.ui.client.hud.element.elements

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.ui.client.hud.designer.GuiHudDesigner
import net.blueheart.hdebug.ui.client.hud.element.Border
import net.blueheart.hdebug.ui.client.hud.element.Element
import net.blueheart.hdebug.ui.client.hud.element.ElementInfo
import net.blueheart.hdebug.ui.client.hud.element.Side
import net.blueheart.hdebug.ui.font.Fonts
import net.blueheart.hdebug.utils.render.AnimationUtils
import net.blueheart.hdebug.utils.render.ColorUtils
import net.blueheart.hdebug.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", single = true)
class Notifications(x: Double = 0.0, y: Double = 30.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = Notification("BlueHeart Dev")

    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        if (HDebug.hud.notifications.size > 0)
            HDebug.hud.notifications[0].drawNotification()

        if (mc.currentScreen is GuiHudDesigner) {
            if (!HDebug.hud.notifications.contains(exampleNotification))
                HDebug.hud.addNotification(exampleNotification)

            exampleNotification.fadeState = Notification.FadeState.STAY
            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-95F, -20F, 0F, 0F)
        }

        return null
    }

}

class Notification(private val message: String) {

    var x = 0F
    var textLength = 0

    private var stay = 0F
    private var fadeStep = 0F
    var fadeState = FadeState.IN

    /**
     * Fade state for animation
     */
    enum class FadeState { IN, STAY, OUT, END }

    init {
        textLength = Fonts.font35.getStringWidth(message)
    }

    /**
     * Draw notification
     */
    fun drawNotification() {
        // Draw notification
        RenderUtils.drawRect(-x + 8 + textLength, 0F, -x, -20F, Color(0, 0, 0).rgb)
        RenderUtils.drawRect(-x, 0F, -x - 5, -20F, ColorUtils.rainbow(400000000L).rgb)
        Fonts.font35.drawString(message, -x + 4, -14F, Int.MAX_VALUE)
        GlStateManager.resetColor()
        // Animation
        val delta = RenderUtils.deltaTime
        val width = textLength + 8F

        when (fadeState) {
            FadeState.IN -> {
                if (x < width) {
                    x = AnimationUtils.easeOut(fadeStep, width) * width
                    fadeStep += delta / 4F
                } else fadeState = FadeState.STAY

                stay = 60F

                if (x > width)
                    x = width
            }

            FadeState.STAY -> if (stay > 0)
                stay = 0F
            else
                fadeState = FadeState.OUT

            FadeState.OUT -> if (x > 0) {
                x = AnimationUtils.easeOut(fadeStep, width) * width
                fadeStep -= delta / 4F
            } else
                fadeState = FadeState.END

            FadeState.END -> HDebug.hud.removeNotification(this)
        }
    }
}