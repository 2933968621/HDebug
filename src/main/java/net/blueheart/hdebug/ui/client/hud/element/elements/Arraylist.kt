package net.blueheart.hdebug.ui.client.hud.element.elements

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.ui.client.hud.designer.GuiHudDesigner
import net.blueheart.hdebug.ui.client.hud.element.Border
import net.blueheart.hdebug.ui.client.hud.element.Element
import net.blueheart.hdebug.ui.client.hud.element.ElementInfo
import net.blueheart.hdebug.ui.client.hud.element.Side
import net.blueheart.hdebug.ui.client.hud.element.Side.Horizontal
import net.blueheart.hdebug.ui.client.hud.element.Side.Vertical
import net.blueheart.hdebug.ui.font.Fonts
import net.blueheart.hdebug.utils.render.AnimationUtils
import net.blueheart.hdebug.utils.render.ColorUtils
import net.blueheart.hdebug.utils.render.RenderUtils
import net.blueheart.hdebug.value.*
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color


/**
 * CustomHUD Arraylist element
 *
 * Shows a list of enabled modules
 */
@ElementInfo(name = "Arraylist", single = true)
class Arraylist(x: Double = 1.0, y: Double = 2.0, scale: Float = 1F,
                side: Side = Side(Horizontal.RIGHT, Vertical.UP)) : Element(x, y, scale, side) {

    private val colorModeValue = ListValue("Text-Color", arrayOf("Custom", "Random", "Rainbow"), "Custom")
    private val rainbowModeValue = ListValue("TextRainbowMode", arrayOf("LiquidBounce","Test"), "LiquidBounce")
    private val colorRedValue = IntegerValue("Text-R", 0, 0, 255)
    private val colorGreenValue = IntegerValue("Text-G", 111, 0, 255)
    private val colorBlueValue = IntegerValue("Text-B", 255, 0, 255)
    private val rectColorModeValue = ListValue("Rect-Color", arrayOf("Custom", "Random", "Rainbow"), "Rainbow")
    private val rectColorRedValue = IntegerValue("Rect-R", 255, 0, 255)
    private val rectColorGreenValue = IntegerValue("Rect-G", 255, 0, 255)
    private val rectColorBlueValue = IntegerValue("Rect-B", 255, 0, 255)
    private val rectColorBlueAlpha = IntegerValue("Rect-Alpha", 255, 0, 255)
    private val saturationValue = FloatValue("Random-Saturation", 0.9f, 0f, 1f)
    private val brightnessValue = FloatValue("Random-Brightness", 1f, 0f, 1f)
    private val tags = BoolValue("Tags", true)
    private val shadow = BoolValue("ShadowText", true)
    private val backgroundColorModeValue = ListValue("Background-Color", arrayOf("Custom", "Random", "Rainbow"), "Custom")
    private val backgroundColorRedValue = IntegerValue("Background-R", 0, 0, 255)
    private val backgroundColorGreenValue = IntegerValue("Background-G", 0, 0, 255)
    private val backgroundColorBlueValue = IntegerValue("Background-B", 0, 0, 255)
    private val backgroundColorAlphaValue = IntegerValue("Background-Alpha", 0, 0, 255)
    private val rectValue = ListValue("Rect", arrayOf("None", "Left", "Right"), "None")
    private val upperCaseValue = BoolValue("UpperCase", false)
    private val spaceValue = FloatValue("Space", 0F, 0F, 5F)
    private val textHeightValue = FloatValue("TextHeight", 11F, 1F, 20F)
    private val textYValue = FloatValue("TextY", 1F, 0F, 20F)
    private val tagsArrayColor = BoolValue("TagsArrayColor", false)
    private val fontValue = FontValue("Font", Fonts.font40)

    private var x2 = 0
    private var y2 = 0F

    private var modules = emptyList<Module>()

    override fun drawElement(): Border? {
        val fontRenderer = fontValue.get()

        // Slide animation - update every render
        val delta = RenderUtils.deltaTime

        for (module in HDebug.moduleManager.modules) {
            if (!module.array || (!module.state && module.slide == 0F)) continue

            var displayString = if (!tags.get())
                module.name
            else if (tagsArrayColor.get())
                module.colorlessTagName
            else module.tagName

            if (upperCaseValue.get())
                displayString = displayString.toUpperCase()

            val width = fontRenderer.getStringWidth(displayString)

            if (module.state) {
                if (module.slide < width) {
                    module.slide = AnimationUtils.easeOut(module.slideStep, width.toFloat()) * width
                    module.slideStep += delta / 4F
                }
            } else if (module.slide > 0) {
                module.slide = AnimationUtils.easeOut(module.slideStep, width.toFloat()) * width
                module.slideStep -= delta / 4F
            }

            module.slide = module.slide.coerceIn(0F, width.toFloat())
            module.slideStep = module.slideStep.coerceIn(0F, width.toFloat())
        }

        // Draw arraylist
        val colorMode = colorModeValue.get()
        val rainbowMode = rainbowModeValue.get()
        val rectColorMode = rectColorModeValue.get()
        val backgroundColorMode = backgroundColorModeValue.get()
        val customColor = Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get(), 1).rgb
        val rectCustomColor = Color(rectColorRedValue.get(), rectColorGreenValue.get(), rectColorBlueValue.get(),
                rectColorBlueAlpha.get()).rgb
        val space = spaceValue.get()
        val textHeight = textHeightValue.get()
        val textY = textYValue.get()
        val rectMode = rectValue.get()
        val backgroundCustomColor = Color(backgroundColorRedValue.get(), backgroundColorGreenValue.get(),
                backgroundColorBlueValue.get(), backgroundColorAlphaValue.get()).rgb
        val textShadow = shadow.get()
        val textSpacer = textHeight + space
        val saturation = saturationValue.get()
        val brightness = brightnessValue.get()

        when (side.horizontal) {
            Horizontal.RIGHT, Horizontal.MIDDLE -> {
                modules.forEachIndexed { index, module ->
                    var displayString = if (!tags.get())
                        module.name
                    else if (tagsArrayColor.get())
                        module.colorlessTagName
                    else module.tagName

                    if (upperCaseValue.get())
                        displayString = displayString.toUpperCase()

                    val xPos = -module.slide - 2
                    val yPos = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) *
                            if (side.vertical == Vertical.DOWN) index + 1 else index
                    val moduleColor = Color.getHSBColor(module.hue, saturation, brightness).rgb

                    RenderUtils.drawRect(
                            xPos - if (rectMode.equals("right", true)) 5 else 2,
                            yPos - if (index == 0) 1 else 0,
                            if (rectMode.equals("right", true)) -3F else 0F,
                            yPos + textHeight, when {
                        backgroundColorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow(400000000L * index).rgb
                        backgroundColorMode.equals("Random", ignoreCase = true) -> moduleColor
                        else -> backgroundCustomColor
                    }
                    )

                    fontRenderer.drawString(displayString, xPos - if (rectMode.equals("right", true)) 3 else 0, yPos + textY, when {
                        colorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow(400000000L * index).rgb
                        colorMode.equals("Random", ignoreCase = true) -> moduleColor
                        else -> customColor
                    }, textShadow)

                    if (!rectMode.equals("none", true)) {
                        val rectColor = when {
                            rectColorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow(400000000L * index).rgb
                            rectColorMode.equals("Random", ignoreCase = true) -> moduleColor
                            else -> rectCustomColor
                        }

                        when {
                            rectMode.equals("left", true) -> RenderUtils.drawRect(xPos - 5, yPos - 1, xPos - 2, yPos + textHeight,
                                    rectColor)
                            rectMode.equals("right", true) -> RenderUtils.drawRect(-3F, yPos - 1F, 0F,
                                    yPos + textHeight, rectColor)
                        }
                    }
                }
            }

            Horizontal.LEFT -> {
                modules.forEachIndexed { index, module ->
                    var displayString = if (!tags.get())
                        module.name
                    else if (tagsArrayColor.get())
                        module.colorlessTagName
                    else module.tagName

                    if (upperCaseValue.get())
                        displayString = displayString.toUpperCase()

                    val width = fontRenderer.getStringWidth(displayString)
                    val xPos = -(width - module.slide) + if (rectMode.equals("left", true)) 5 else 2
                    val yPos = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) *
                            if (side.vertical == Vertical.DOWN) index + 1 else index
                    val moduleColor = Color.getHSBColor(module.hue, saturation, brightness).rgb

                    RenderUtils.drawRect(
                            0F,
                            yPos - if (index == 0) 1 else 0,
                            xPos + width + if (rectMode.equals("right", true)) 5 else 2,
                            yPos + textHeight, when {
                        backgroundColorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow(400000000L * index).rgb
                        backgroundColorMode.equals("Random", ignoreCase = true) -> moduleColor
                        else -> backgroundCustomColor
                    }
                    )

                    fontRenderer.drawString(displayString, xPos, yPos + textY, when {
                        colorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow(400000000L * index).rgb
                        colorMode.equals("Random", ignoreCase = true) -> moduleColor
                        else -> customColor
                    }, textShadow)

                    if (!rectMode.equals("none", true)) {
                        val rectColor = when {
                            rectColorMode.equals("Rainbow", ignoreCase = true) -> ColorUtils.rainbow(400000000L * index).rgb
                            rectColorMode.equals("Random", ignoreCase = true) -> moduleColor
                            else -> rectCustomColor
                        }

                        when {
                            rectMode.equals("left", true) -> RenderUtils.drawRect(0F,
                                    yPos - 1, 3F, yPos + textHeight, rectColor)
                            rectMode.equals("right", true) ->
                                RenderUtils.drawRect(xPos + width + 2, yPos - 1, xPos + width + 2 + 3,
                                        yPos + textHeight, rectColor)
                        }
                    }
                }
            }
        }

        // Draw border
        if (mc.currentScreen is GuiHudDesigner) {
            x2 = Int.MIN_VALUE

            if (modules.isEmpty()) {
                return if (side.horizontal == Horizontal.LEFT)
                    Border(0F, -1F, 20F, 20F)
                else
                    Border(0F, -1F, -20F, 20F)
            }

            for (module in modules) {
                when (side.horizontal) {
                    Horizontal.RIGHT, Horizontal.MIDDLE -> {
                        val xPos = -module.slide.toInt() - 2
                        if (x2 == Int.MIN_VALUE || xPos < x2) x2 = xPos
                    }
                    Horizontal.LEFT -> {
                        val xPos = module.slide.toInt() + 14
                        if (x2 == Int.MIN_VALUE || xPos > x2) x2 = xPos
                    }
                }
            }
            y2 = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) * modules.size

            return Border(0F, -1F, x2 - 7F, y2)
        }
        GlStateManager.resetColor()
        return null
    }

    override fun updateElement() {
        modules = HDebug.moduleManager.modules
                .filter { it.array && it.slide > 0 }
                .sortedBy { -fontValue.get().getStringWidth(if (upperCaseValue.get()) (if (!tags.get()) it.name else if (tagsArrayColor.get()) it.colorlessTagName else it.tagName).toUpperCase() else if (!tags.get()) it.name else if (tagsArrayColor.get()) it.colorlessTagName else it.tagName) }
    }
}