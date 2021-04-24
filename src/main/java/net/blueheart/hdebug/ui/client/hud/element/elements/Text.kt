package net.blueheart.hdebug.ui.client.hud.element.elements

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.ui.client.hud.designer.GuiHudDesigner
import net.blueheart.hdebug.ui.client.hud.element.Border
import net.blueheart.hdebug.ui.client.hud.element.Element
import net.blueheart.hdebug.ui.client.hud.element.ElementInfo
import net.blueheart.hdebug.ui.client.hud.element.Side
import net.blueheart.hdebug.ui.font.Fonts
import net.blueheart.hdebug.utils.CPSCounter
import net.blueheart.hdebug.utils.EntityUtils
import net.blueheart.hdebug.utils.ServerUtils
import net.blueheart.hdebug.utils.render.ColorUtils
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FontValue
import net.blueheart.hdebug.value.IntegerValue
import net.blueheart.hdebug.value.TextValue
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatAllowedCharacters
import org.lwjgl.input.Keyboard
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.Aura
import java.awt.Color
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import kotlin.math.sqrt

/**
 * CustomHUD text element
 *
 * Allows to draw custom text
 */
@ElementInfo(name = "Text")
class Text(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F,
           side: Side = Side.default()) : Element(x, y, scale, side) {

    companion object {
        var killAura = HDebug.moduleManager.getModule(Aura::class.java) as Aura?
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")
        val HOUR_FORMAT = SimpleDateFormat("HH:mm")

        val DECIMAL_FORMAT = DecimalFormat("0.00")

        /**
         * Create default element
         */
        fun defaultClient(): Text {
            val text = Text(x = 2.0, y = 2.0, scale = 2F)

            text.displayString.set("%ClientName%")
            text.shadow.set(true)
            text.fontValue.set(Fonts.font40)
            text.setColor(Color(0, 255, 234))

            return text
        }

    }

    private val displayString = TextValue("DisplayText", "")
    private val redValue = IntegerValue("Red", 255, 0, 255)
    private val greenValue = IntegerValue("Green", 255, 0, 255)
    private val blueValue = IntegerValue("Blue", 255, 0, 255)
    private val rainbow = BoolValue("Rainbow", false)
    private val shadow = BoolValue("Shadow", true)
    private var fontValue = FontValue("Font", Fonts.font40)

    private var editMode = false
    private var editTicks = 0
    private var prevClick = 0L

    private var displayText = display

    private val display: String
        get() {
            val textContent = if (displayString.get().isEmpty() && !editMode)
                "Text Element"
            else
                displayString.get()


            return multiReplace(textContent)
        }

    private fun getReplacement(str: String): String? {
        if (mc.thePlayer != null) {
            when (str) {
                "x" -> return DECIMAL_FORMAT.format(mc.thePlayer.posX)
                "y" -> return DECIMAL_FORMAT.format(mc.thePlayer.posY)
                "z" -> return DECIMAL_FORMAT.format(mc.thePlayer.posZ)
                "xdp" -> return mc.thePlayer.posX.toString()
                "ydp" -> return mc.thePlayer.posY.toString()
                "zdp" -> return mc.thePlayer.posZ.toString()
                "velocity" -> return DECIMAL_FORMAT.format(sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ))
                "ping" -> return EntityUtils.getPing(mc.thePlayer).toString()
            }
        }

        return when (str) {
            "UserName" -> mc.getSession().username
            "ClientName" -> "HDebug"
            "ClientVersion" -> HDebug.CLIENT_VERSION.toString()
            "ClientCreator" -> HDebug.CLIENT_CREATOR
            "ClientDev" -> HDebug.CLIENT_CREATOR
            "HDebug" -> "世界上最尴尬的事情就是，别人根本没把你当回事，你自己还在那里多愁善感，所以说永远不要高估你在别人心中的地位"
            "QQ" -> "2933968621"
            "Group" -> "1128533970"
            "BlueHeart" -> "喵~"
            "Dev" -> "蓝心:咕咕咕"
            "CD" -> "你发现了一个彩蛋QWQ"
            "Code" -> "Code By NMH Teams"
            "OldName" -> "ExcessiveKill"
            "HP" -> mc.thePlayer.health.toString()
            "Aura" -> if(killAura?.state == true){"true"}else{"false"}
            "fps" -> Minecraft.getDebugFPS().toString()
            "date" -> DATE_FORMAT.format(System.currentTimeMillis())
            "time" -> HOUR_FORMAT.format(System.currentTimeMillis())
            "ServerIp" -> ServerUtils.getRemoteIp()
            "cps", "lcps" -> return CPSCounter.getCPS(CPSCounter.MouseButton.LEFT).toString()
            "mcps" -> return CPSCounter.getCPS(CPSCounter.MouseButton.MIDDLE).toString()
            "rcps" -> return CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT).toString()
            else -> null // Null = don't replace
        }
    }

    private fun multiReplace(str: String): String {
        var lastPercent = -1
        val result = StringBuilder()
        for (i in str.indices) {
            if (str[i] == '%') {
                if (lastPercent != -1) {
                    if (lastPercent + 1 != i) {
                        val replacement = getReplacement(str.substring(lastPercent + 1, i))

                        if (replacement != null) {
                            result.append(replacement)
                            lastPercent = -1
                            continue
                        }
                    }
                    result.append(str, lastPercent, i)
                }
                lastPercent = i
            } else if (lastPercent == -1) {
                result.append(str[i])
            }
        }

        if (lastPercent != -1) {
            result.append(str, lastPercent, str.length)
        }

        return result.toString()
    }

    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        val color = Color(redValue.get(), greenValue.get(), blueValue.get()).rgb

        val fontRenderer = fontValue.get()

        fontRenderer.drawString(displayText, 0F, 0F, if (rainbow.get())
            ColorUtils.rainbow(400000000L).rgb else color, shadow.get())

        if (editMode && mc.currentScreen is GuiHudDesigner && editTicks <= 40)
            fontRenderer.drawString("_", fontRenderer.getStringWidth(displayText) + 2F,
                    0F, if (rainbow.get()) ColorUtils.rainbow(400000000L).rgb else color, shadow.get())

        if (editMode && mc.currentScreen !is GuiHudDesigner) {
            editMode = false
            updateElement()
        }

        return Border(
                -2F,
                -2F,
                fontRenderer.getStringWidth(displayText) + 2F,
                fontRenderer.FONT_HEIGHT.toFloat()
        )
    }

    override fun updateElement() {
        editTicks += 5
        if (editTicks > 80) editTicks = 0

        displayText = if (editMode) displayString.get() else display
    }

    override fun handleMouseClick(x: Double, y: Double, mouseButton: Int) {
        if (isInBorder(x, y) && mouseButton == 0) {
            if (System.currentTimeMillis() - prevClick <= 250L)
                editMode = true

            prevClick = System.currentTimeMillis()
        } else {
            editMode = false
        }
    }

    override fun handleKey(c: Char, keyCode: Int) {
        if (editMode && mc.currentScreen is GuiHudDesigner) {
            if (keyCode == Keyboard.KEY_BACK) {
                if (displayString.get().isNotEmpty())
                    displayString.set(displayString.get().substring(0, displayString.get().length - 1))

                updateElement()
                return
            }

            if (ChatAllowedCharacters.isAllowedCharacter(c) || c == '§')
                displayString.set(displayString.get() + c)

            updateElement()
        }
    }

    fun setColor(c: Color): Text {
        redValue.set(c.red)
        greenValue.set(c.green)
        blueValue.set(c.blue)
        return this
    }

}