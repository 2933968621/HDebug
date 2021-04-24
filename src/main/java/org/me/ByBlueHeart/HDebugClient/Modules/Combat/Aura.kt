package org.me.ByBlueHeart.HDebugClient.Modules.Combat

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.*
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.features.module.modules.combat.NoFriends
import net.blueheart.hdebug.features.module.modules.player.Blink
import net.blueheart.hdebug.features.module.modules.render.FreeCam
import net.blueheart.hdebug.ui.client.hud.element.elements.Notification
import net.blueheart.hdebug.utils.EntityUtils
import net.blueheart.hdebug.utils.RaycastUtils
import net.blueheart.hdebug.utils.RotationUtils
import net.blueheart.hdebug.utils.extensions.getDistanceToEntityBox
import net.blueheart.hdebug.utils.misc.RandomUtils
import net.blueheart.hdebug.utils.render.RenderManagers
import net.blueheart.hdebug.utils.render.RenderUtils
import net.blueheart.hdebug.utils.timer.MSTimer
import net.blueheart.hdebug.utils.timer.TimeUtils
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue
import net.blueheart.hdebug.value.IntegerValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGameOver
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.settings.KeyBinding
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.*
import net.minecraft.potion.Potion
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.MathHelper
import net.minecraft.world.WorldSettings
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.Cylinder
import org.me.ByBlueHeart.HDebugClient.HDebugMain
import org.me.ByBlueHeart.HDebugClient.Modules.Misc.AntiBot
import org.me.ByBlueHeart.HDebugClient.Modules.Misc.Teams
import java.awt.Color
import java.util.*
import kotlin.jvm.internal.Intrinsics
import kotlin.math.max
import kotlin.math.min

@ModuleInfo(name = "Aura", description = "Automatically attacks targets around you.", category = ModuleCategory.COMBAT, keyBind = Keyboard.KEY_R)
class Aura : Module() {
    var AuraHitAnimationsSetup = 0.0
    var isUp = false
    /**
     * OPTIONS
     */
    // CPS - Attack speed
    private val maxCPS: IntegerValue = object : IntegerValue("MaxCPS", 12, 1, 50) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = minCPS.get()
            if (i > newValue) set(i)

            attackDelay = TimeUtils.randomClickDelay(minCPS.get(), this.get())
        }
    }

    private val minCPS: IntegerValue = object : IntegerValue("MinCPS", 8, 1, 50) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val i = maxCPS.get()
            if (i < newValue) set(i)

            attackDelay = TimeUtils.randomClickDelay(this.get(), maxCPS.get())
        }
    }

    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 20)

    // Range
    private val rangeValue = FloatValue("Range", 3.7f, 1f, 10f)
    private val throughWallsRangeValue = FloatValue("ThroughWallsRange", 3f, 0f, 8f)
    private val rangeSprintReducementValue = FloatValue("RangeSprintReducement", 0f, 0f, 0.5f)

    // Modes
    private val priorityValue = ListValue("Priority", arrayOf("Health", "Distance", "Fov", "LivingTime"), "Distance")
    private val targetModeValue = ListValue("TargetMode", arrayOf("Single", "Switch", "Multi"), "Switch")
    private val markValue = ListValue("TargetESP", arrayOf("Box", "Normal", "Circle", "None"), "Normal")
    private val AutoBlockModeValue = ListValue("AutoBlockMode", arrayOf("Packet","Vanilla"), "Vanilla")
    private val targetespR = IntegerValue("Red", 255, 0, 255)
    private val targetespG = IntegerValue("Green", 255, 0, 255)
    private val targetespB = IntegerValue("Blue", 255, 0, 255)
    private val targetespA = IntegerValue("Alpha", 255, 0, 255)

    // Bypass
    private val swingValue = BoolValue("Swing", true)
    private val keepSprintValue = BoolValue("KeepSprint", true)
    private val lockcenter = BoolValue("LockCenter", true)

    // AutoBlock
    private val autoBlockValue = BoolValue("AutoBlock", false)
    private val interactAutoBlockValue = BoolValue("InteractAutoBlock", true)
    private val delayedBlockValue = BoolValue("DelayedBlock", true)
    private val blockRate = IntegerValue("BlockRate", 100, 1, 1000)
    private val hytdelayValue = IntegerValue("HYTDelay", 10, 0, 100)
    private val switchDelayValue: IntegerValue = object : IntegerValue("SwitchDelay", 100, 0, 1000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            if (0 > newValue) set(0)
        }
    }
    // Raycast
    private val raycastValue = BoolValue("RayCast", true)
    private val raycastIgnoredValue = BoolValue("RayCastIgnored", false)
    private val livingRaycastValue = BoolValue("LivingRayCast", true)

    // Bypass
    private val aacValue = BoolValue("AAC", false)
    private val HYTValue = BoolValue("HuaYuTing", false)
    private val Rede2Value = BoolValue("RedeSky", false)

    // Turn Speed
    private val maxTurnSpeed: FloatValue = object : FloatValue("MaxTurnSpeed", 36f, 0f, 360f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = minTurnSpeed.get()
            if (v > newValue) set(v)
        }
    }

    private val minTurnSpeed: FloatValue = object : FloatValue("MinTurnSpeed", 180f, 0f, 360f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = maxTurnSpeed.get()
            if (v < newValue) set(v)
        }
    }

    private val silentRotationValue = BoolValue("SilentRotation", true)
    private val rotationStrafeValue = ListValue("Strafe", arrayOf("Off", "Strict", "Silent"), "Off")
    private val randomCenterValue = BoolValue("RandomCenter", true)
    private val outborderValue = BoolValue("Outborder", false)
    private val fovValue = FloatValue("FOV", 360f, 0f, 360f)

    // Predict
    private val predictValue = BoolValue("Predict", true)

    private val maxPredictSize: FloatValue = object : FloatValue("MaxPredictSize", 1f, 0.1f, 5f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = minPredictSize.get()
            if (v > newValue) set(v)
        }
    }

    private val minPredictSize: FloatValue = object : FloatValue("MinPredictSize", 1f, 0.1f, 5f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            val v = maxPredictSize.get()
            if (v < newValue) set(v)
        }
    }

    // Bypass
    private val failRateValue = FloatValue("FailRate", 0f, 0f, 100f)
    private val fakeSwingValue = BoolValue("FakeSwing", true)
    private val noInventoryAttackValue = BoolValue("NoInvAttack", false)
    private val noInventoryDelayValue = IntegerValue("NoInvDelay", 200, 0, 500)
    private val limitedMultiTargetsValue = IntegerValue("LimitedMultiTargets", 0, 0, 50)

    // Visuals
    private val fakeSharpValue = BoolValue("FakeSharp", true)
    private val disable = BoolValue("AutoDisable", true)

    /**
     * MODULE
     */
    var Timer = MSTimer()
    // Target
    var target: EntityLivingBase? = null
    private var currentTarget: EntityLivingBase? = null
    private var hitable = false
    private val prevTargetEntities = mutableListOf<Int>()

    // Attack delay
    private val attackTimer = MSTimer()
    private var attackDelay = 0L
    private var clicks = 0
    // Switch delay
    private val switchDelayTimer = MSTimer()
    // Container Delay
    private var containerOpen = -1L

    // Fake block status
    var blockingStatus = false

    /**
     * Enable kill aura module
     */
    override fun onEnable() {
        AuraHitAnimationsSetup = 0.0
        isUp = false
        mc.thePlayer ?: return
        mc.theWorld ?: return

        updateTarget()
    }

    /**
     * Disable kill aura module
     */
    override fun onDisable() {
        target = null
        currentTarget = null
        hitable = false
        prevTargetEntities.clear()
        attackTimer.reset()
        clicks = 0
        stopBlocking()
    }

    /**
     * Motion event
     */
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (event.eventState == EventState.POST) {
            target ?: return
            currentTarget ?: return

            // Update hitable
            updateHitable()

            // AutoBlock
            if (autoBlockValue.get() && delayedBlockValue.get() && canBlock)
                startBlocking(currentTarget!!, hitable)

            return
        }

        if (rotationStrafeValue.get().equals("Off", true))
            update()
    }

    /**
     * Strafe event
     */
    @EventTarget
    fun onStrafe(event: StrafeEvent) {
        if (rotationStrafeValue.get().equals("Off", true))
            return

        update()

        if (currentTarget != null && RotationUtils.targetRotation != null) {
            when (rotationStrafeValue.get().toLowerCase()) {
                "strict" -> {
                    val (yaw) = RotationUtils.targetRotation ?: return
                    var strafe = event.strafe
                    var forward = event.forward
                    val friction = event.friction

                    var f = strafe * strafe + forward * forward

                    if (f >= 1.0E-4F) {
                        f = MathHelper.sqrt_float(f)

                        if (f < 1.0F)
                            f = 1.0F

                        f = friction / f
                        strafe *= f
                        forward *= f

                        val yawSin = MathHelper.sin((yaw * Math.PI / 180F).toFloat())
                        val yawCos = MathHelper.cos((yaw * Math.PI / 180F).toFloat())

                        mc.thePlayer.motionX += strafe * yawCos - forward * yawSin
                        mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin
                    }
                    event.cancelEvent()
                }
                "silent" -> {
                    update()

                    RotationUtils.targetRotation.applyStrafeToPlayer(event)
                    event.cancelEvent()
                }
            }
        }
    }

    fun update() {
        if (cancelRun || (noInventoryAttackValue.get() && (mc.currentScreen is GuiContainer ||
                        System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get()))) return

        // Update target
        updateTarget()

        if (target == null) {
            stopBlocking()
            return
        }

        // Target
        currentTarget = target

        if (!targetModeValue.get().equals("Switch", ignoreCase = true) && isEnemy(currentTarget)) // TO DO		        if (!targetModeValue.get().equals("Switch", ignoreCase = true) && isEnemy(currentTarget)) // TO DO
            if (switchDelayTimer.hasTimePassed(switchDelayValue.get().toLong())) {		            target = currentTarget
                target = currentTarget
                switchDelayTimer.reset()
            }
    }

    /**
     * Update event
     */
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if(HDebugMain.nice != true){
            System.exit(0)
        }
        if (disable.get()) {
            if ((!mc.thePlayer.isEntityAlive()
                            || (mc.currentScreen != null && mc.currentScreen is GuiGameOver))) {
                this.toggle();
                HDebug.hud.addNotification(Notification("Aura disabled due to death."))
                return;
            }
            if (mc.thePlayer.ticksExisted <= 1) {
                this.toggle();
                HDebug.hud.addNotification(Notification("Aura disabled due to death."))
                return;
            }
        }
        if (cancelRun) {
            target = null
            currentTarget = null
            hitable = false
            stopBlocking()
            return
        }

        if (noInventoryAttackValue.get() && (mc.currentScreen is GuiContainer ||
                        System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())) {
            target = null
            currentTarget = null
            hitable = false
            if (mc.currentScreen is GuiContainer) containerOpen = System.currentTimeMillis()
            return
        }

        if (target != null && currentTarget != null) {
            while (clicks > 0) {
                runAttack()
                clicks--
            }
        }
    }

    /**
     * Render event
     */
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (cancelRun) {
            target = null
            currentTarget = null
            hitable = false;
            stopBlocking();
            return;
        }

        if (noInventoryAttackValue.get() && (mc.currentScreen is GuiContainer ||
                        System.currentTimeMillis() - containerOpen < noInventoryDelayValue.get())) {
            target = null
            currentTarget = null
            hitable = false
            if (mc.currentScreen is GuiContainer) containerOpen = System.currentTimeMillis()
            return
        }

        target ?: return

        if (targetModeValue.get().equals("Multi", ignoreCase = true)) {
            when (markValue.get().toLowerCase()) {
                "box" -> {
                    if (target == null)
                        Intrinsics.throwNpe()
                    RenderUtils.drawEntityBox(target, if (target!!.hurtTime > 3) Color(targetespR.get(), targetespG.get(), targetespB.get(), targetespA.get()) else Color(255, 50, 50, 75),true)
                }
                "normal" -> {
                    if (target == null)
                        Intrinsics.throwNpe()
                    RenderUtils.drawPlatform(target, if (hitable) Color(37, 126, 255, 70) else Color(255, 0, 0, 70))
                }
                "circle" -> {
                    val x1: Double = target!!.lastTickPosX + (target!!.posX - target!!.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                    val y1: Double = target!!.lastTickPosY + (target!!.posY - target!!.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
                    val z1: Double = target!!.lastTickPosZ + (target!!.posZ - target!!.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                    if (AuraHitAnimationsSetup > target!!.getEyeHeight() + 0.4 || AuraHitAnimationsSetup < 0.0) {
                        isUp = !isUp
                    }
                    if (isUp) {
                        val auraHitAnimationsSetup: Double = AuraHitAnimationsSetup
                        val n = 3.0
                        AuraHitAnimationsSetup = auraHitAnimationsSetup + n / Minecraft.getDebugFPS()
                    } else {
                        val auraHitAnimationsSetup2: Double = AuraHitAnimationsSetup
                        val n2 = 3.0
                        AuraHitAnimationsSetup = auraHitAnimationsSetup2 - n2 / Minecraft.getDebugFPS()
                    }
                    if (isUp) {
                        for (i in 0..99) {
                            this.esp(target, x1, y1 + AuraHitAnimationsSetup - i * 0.005, z1, (200 - i) * 0.0015f)
                        }
                    } else {
                        for (i in 0..99) {
                            this.esp(target, x1, y1 + AuraHitAnimationsSetup + i * 0.005, z1, (200 - i) * 0.0015f)
                        }
                    }
                }
            }
        }
        if (targetModeValue.get().equals("Switch", ignoreCase = true)) {
            when (markValue.get().toLowerCase()) {
                "box" -> {
                    if (target == null)
                        Intrinsics.throwNpe()
                    RenderUtils.drawEntityBox(target, if (target!!.hurtTime > 3) Color(targetespR.get(), targetespG.get(), targetespB.get(), targetespA.get()) else Color(255, 50, 50, 75),true)
                }
                "normal" -> {
                    if (target == null)
                        Intrinsics.throwNpe()
                    RenderUtils.drawPlatform(target, if (hitable) Color(37, 126, 255, 70) else Color(255, 0, 0, 70))
                }
                "circle" -> {
                    val x1: Double = target!!.lastTickPosX + (target!!.posX - target!!.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                    val y1: Double = target!!.lastTickPosY + (target!!.posY - target!!.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY
                    val z1: Double = target!!.lastTickPosZ + (target!!.posZ - target!!.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                    if (AuraHitAnimationsSetup > target!!.getEyeHeight() + 0.4 || AuraHitAnimationsSetup < 0.0) {
                        isUp = !isUp
                    }
                    if (isUp) {
                        val auraHitAnimationsSetup: Double = AuraHitAnimationsSetup
                        val n = 3.0
                        AuraHitAnimationsSetup = auraHitAnimationsSetup + n / Minecraft.getDebugFPS()
                    } else {
                        val auraHitAnimationsSetup2: Double = AuraHitAnimationsSetup
                        val n2 = 3.0
                        AuraHitAnimationsSetup = auraHitAnimationsSetup2 - n2 / Minecraft.getDebugFPS()
                    }
                    if (isUp) {
                        for (i in 0..99) {
                            this.esp(target, x1, y1 + AuraHitAnimationsSetup - i * 0.005, z1, (200 - i) * 0.0015f)
                        }
                    } else {
                        for (i in 0..99) {
                            this.esp(target, x1, y1 + AuraHitAnimationsSetup + i * 0.005, z1, (200 - i) * 0.0015f)
                        }
                    }
                }
                "cylinder" -> {
                    val posX: Double = (target!!.lastTickPosX
                            + (target!!.posX - target!!.lastTickPosX) * event.partialTicks as Double
                            - RenderManagers.renderPosX)
                    val posY: Double = (target!!.lastTickPosY
                            + (target!!.posY - target!!.lastTickPosY) * event.partialTicks as Double
                            - RenderManagers.renderPosY)
                    val posZ: Double = (target!!.lastTickPosZ
                            + (target!!.posZ - target!!.lastTickPosZ) * event.partialTicks as Double
                            - RenderManagers.renderPosZ)

                    if (target!!.hurtTime > 0) {
                        RenderUtils.drawWolframEntityESP(target, Color(255, 102, 113).rgb, posX, posY, posZ)
                    } else {
                        RenderUtils.drawWolframEntityESP(target, Color(186, 100, 200).rgb, posX, posY, posZ)
                    }
                }
            }
        }

        if (currentTarget != null && attackTimer.hasTimePassed(attackDelay) &&
                currentTarget!!.hurtTime <= hurtTimeValue.get()) {
            clicks++
            attackTimer.reset()
            attackDelay = TimeUtils.randomClickDelay(minCPS.get(), maxCPS.get())
        }
    }

    /**
     * Handle entity move
     */
    open fun esp(player: Entity?, x: Double, y: Double, z: Double, alpha: Float): Unit {
        val c = Cylinder()
        GL11.glPushMatrix()
        GL11.glDisable(2896)
        GL11.glDisable(3553)
        GL11.glEnable(3042)
        GL11.glBlendFunc(770, 771)
        GL11.glDisable(2929)
        GL11.glEnable(2848)
        GL11.glDepthMask(true)
        GlStateManager.translate(x, y, z)
        GlStateManager.color(255.0f, 255.0f, 255.0f, alpha)
        GlStateManager.rotate(180.0f, 90.0f, 0.0f, 2.0f)
        GlStateManager.rotate(180.0f, 0.0f, 90.0f, 90.0f)
        c.drawStyle = 100011
        c.draw(0.8f, 0.8f, -0.0f, 360, 0)
        GL11.glDisable(2848)
        GL11.glEnable(2929)
        GL11.glDisable(3042)
        GL11.glEnable(2896)
        GL11.glEnable(3553)
        GL11.glPopMatrix()
    }

    @EventTarget
    fun onEntityMove(event: EntityMovementEvent) {
        val movedEntity = event.movedEntity
        if (target == null || movedEntity !== currentTarget) return
        updateHitable()
    }

    /**
     * Attack enemy
     */
    private fun runAttack() {
        target ?: return
        currentTarget ?: return

        // Settings
        val failRate = failRateValue.get()
        val swing = swingValue.get()
        val multi = targetModeValue.get().equals("Multi", ignoreCase = true)
        val openInventory = aacValue.get() && mc.currentScreen is GuiInventory || HYTValue.get() && mc.currentScreen is GuiInventory || Rede2Value.get() && mc.currentScreen is GuiInventory
        val failHit = failRate > 0 && Random().nextInt(100) <= failRate

        // Close inventory when open
        if (openInventory)
            mc.netHandler.addToSendQueue(C0DPacketCloseWindow())

        // Check is not hitable or check failrate
        if (!hitable || failHit) {
            if (swing && (fakeSwingValue.get() || failHit)) mc.thePlayer.swingItem()
        } else {
            // Attack
            if (!multi) {
                attackEntity(currentTarget!!)
            } else {
                var targets = 0

                for (entity in mc.theWorld.loadedEntityList) {
                    val distance = mc.thePlayer.getDistanceToEntityBox(entity)

                    if (entity is EntityLivingBase && isEnemy(entity) && distance <= getRange(entity)) {
                        attackEntity(entity)

                        targets += 1

                        if (limitedMultiTargetsValue.get() != 0 && limitedMultiTargetsValue.get() <= targets)
                            break
                    }
                    if (Timer.hasTimePassed((hytdelayValue.get() * 10).toLong()) && hytdelayValue.get() != 10) {
                        if (target == null) Intrinsics.throwNpe()
                        if (currentTarget == null) Intrinsics.throwNpe()
                        prevTargetEntities.add(Integer.valueOf(if (HYTValue.get() || Rede2Value.get()) target!!.entityId else currentTarget!!.entityId))
                        Timer.reset()
                    }
                }
            }

            prevTargetEntities.add((if (aacValue.get()) target!!.entityId else currentTarget!!.entityId))

            if (target == currentTarget)
                target = null
        }

        // Open inventory
        if (openInventory)
            mc.netHandler.addToSendQueue(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
    }

    /**
     * Update current target
     */

    private fun updateTarget() {
        // Reset fixed target to null
        target = null

        // Settings
        val hurtTime = hurtTimeValue.get()
        val fov = fovValue.get()
        val switchMode = targetModeValue.get().equals("Switch", ignoreCase = true)

        // Find possible targets
        val targets = mutableListOf<EntityLivingBase>()

        for (entity in mc.theWorld.loadedEntityList) {
            val doSwitch = (switchMode && prevTargetEntities.contains(entity.entityId) && switchDelayTimer.hasTimePassed(switchDelayValue.get().toLong()))
            if (entity !is EntityLivingBase || !isEnemy(entity) || doSwitch) {
                if (doSwitch) {
                    switchDelayTimer.reset()
                }
                continue
            }
            val distance = mc.thePlayer.getDistanceToEntityBox(entity)
            val entityFov = RotationUtils.getRotationDifference(entity)

            if (distance <= maxRange && (fov == 180F || entityFov <= fov) && entity.hurtTime <= hurtTime)
                targets.add(entity)
        }

        // Cleanup last targets when no targets found and try again
        if (targets.isEmpty()) {
            if (prevTargetEntities.isNotEmpty()) {
                prevTargetEntities.clear()
                updateTarget()
            }

            return
        }

        // Sort targets by priority
        when (priorityValue.get().toLowerCase()) {
            "distance" -> targets.sortBy { mc.thePlayer.getDistanceToEntityBox(it) } // Sort by distance
            "health" -> targets.sortBy { it.health } // Sort by health
            "fov" -> targets.sortBy { RotationUtils.getRotationDifference(it) } // Sort by FOV
            "livingtime" -> targets.sortBy { -it.ticksExisted } // Sort by existence
        }

        // Find best target
        for (entity in targets) {
            // Update rotations to current target
            if (!updateRotations(entity)) // when failed then try another target
                continue

            // Set target to current entity
            target = entity
            return
        }
    }

    /**
     * Check if [entity] is selected as enemy with current target options and other modules
     */
    private fun isEnemy(entity: Entity?): Boolean {
        if (entity is EntityLivingBase && (EntityUtils.targetDead || isAlive(entity)) && entity != mc.thePlayer) {
            if (!EntityUtils.targetInvisible && entity.isInvisible())
                return false

            if (EntityUtils.targetPlayer && entity is EntityPlayer) {
                if (entity.isSpectator || AntiBot.isBot(entity))
                    return false

                if (EntityUtils.isFriend(entity) && !HDebug.moduleManager[NoFriends::class.java]!!.state)
                    return false

                val teams = HDebug.moduleManager[Teams::class.java] as Teams

                return !teams.state || !teams.isInYourTeam(entity)
            }

            return EntityUtils.targetMobs && EntityUtils.isMob(entity) || EntityUtils.targetAnimals &&
                    EntityUtils.isAnimal(entity)
        }

        return false
    }

    /**
     * Attack [entity]
     */
    private fun attackEntity(entity: EntityLivingBase) {
        // Stop blocking
        if (mc.thePlayer.isBlocking || blockingStatus) {
            mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN, EnumFacing.DOWN))
            blockingStatus = false
        }

        // Call attack event
        HDebug.eventManager.callEvent(AttackEvent(entity))

        // Attack target
        if (swingValue.get())
            mc.thePlayer.swingItem()
        mc.netHandler.addToSendQueue(C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK))

        if (keepSprintValue.get()) {
            // Critical Effect
            if (mc.thePlayer.fallDistance > 0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder &&
                    !mc.thePlayer.isInWater && !mc.thePlayer.isPotionActive(Potion.blindness) && !mc.thePlayer.isRiding)
                mc.thePlayer.onCriticalHit(entity)

            // Enchant Effect
            if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.heldItem, entity.creatureAttribute) > 0F)
                mc.thePlayer.onEnchantmentCritical(entity)
        } else {
            if (mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR)
                mc.thePlayer.attackTargetEntityWithCurrentItem(entity)
        }

        // Extra critical effects
        val criticals = HDebug.moduleManager[Criticals::class.java] as Criticals

        for (i in 0..2) {
            // Critical Effect
            if (mc.thePlayer.fallDistance > 0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder && !mc.thePlayer.isInWater && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null || criticals.state && criticals.msTimer.hasTimePassed(criticals.delayValue.get().toLong()) && !mc.thePlayer.isInWater && !mc.thePlayer.isInLava && !mc.thePlayer.isInWeb)
                mc.thePlayer.onCriticalHit(target)

            // Enchant Effect
            if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.heldItem, target!!.creatureAttribute) > 0.0f || fakeSharpValue.get())
                mc.thePlayer.onEnchantmentCritical(target)
        }

        // Start blocking after attack
        if (mc.thePlayer.isBlocking || (autoBlockValue.get() && canBlock)) {
            if (!(blockRate.get() > 0 && Random().nextInt(100) <= blockRate.get()))
                return

            if (delayedBlockValue.get())
                return

            startBlocking(entity, interactAutoBlockValue.get())
        }
    }

    /**
     * Update killaura rotations to enemy
     */
    private fun updateRotations(entity: Entity): Boolean {
        if(maxTurnSpeed.get() <= 0F)
            return true

        var boundingBox = entity.entityBoundingBox

        if (predictValue.get())
            boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
            )

        val (vec, rotation) = RotationUtils.searchCenter(
                boundingBox,
                outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2),
                randomCenterValue.get(),
                predictValue.get(),
                mc.thePlayer.getDistanceToEntityBox(entity) < throughWallsRangeValue.get()
        ) ?: return false

        val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation,
                if (lockcenter.get())
                    RotationUtils.toRotation(RotationUtils.getCenter(entity.entityBoundingBox), predictValue.get())
                else

                    rotation


                ,
                (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat())

        val rotation1 = RotationUtils.limitAngleChange(
                RotationUtils.serverRotation, RotationUtils.toRotation(RotationUtils.getCenter(entity.entityBoundingBox), predictValue.get()), (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get()).toFloat()
        )

        if (silentRotationValue.get())
            RotationUtils.setTargetRotation(limitedRotation, if (aacValue.get()) if (HYTValue.get()) 15 else (if (Rede2Value.get()) 5 else 15) else (if (HYTValue.get()) 15 else if (Rede2Value.get()) 5 else 0)) //Bypass HuaYuTing Server
        else
            limitedRotation.toPlayer(mc.thePlayer)

        return true
    }

    /**
     * Check if enemy is hitable with current rotations
     */
    private fun updateHitable() {
        // Disable hitable check if turn speed is zero
        if(maxTurnSpeed.get() <= 0F) {
            hitable = true
            return
        }
        val reach = min(maxRange.toDouble(), mc.thePlayer.getDistanceToEntityBox(target!!)) + 0.4

        if (raycastValue.get()) {
            val raycastedEntity = RaycastUtils.raycastEntity(reach) {
                (!livingRaycastValue.get() || it is EntityLivingBase && it !is EntityArmorStand) &&
                        (isEnemy(it) || raycastIgnoredValue.get() || aacValue.get() || HYTValue.get() && mc.theWorld.getEntitiesWithinAABBExcludingEntity(it, it.entityBoundingBox).isNotEmpty() || Rede2Value.get() && mc.theWorld.getEntitiesWithinAABBExcludingEntity(it, it.entityBoundingBox).isNotEmpty())
            }

            if (raycastValue.get() && raycastedEntity is EntityLivingBase
                    && (HDebug.moduleManager[NoFriends::class.java]!!.state || !EntityUtils.isFriend(raycastedEntity)))
                currentTarget = raycastedEntity

            hitable = if(maxTurnSpeed.get() > 0F) currentTarget == raycastedEntity else true
        } else
            hitable = RotationUtils.isFaced(currentTarget, reach)
    }

    /**
     * Start blocking
     */
    private fun startBlocking(interactEntity: Entity, interact: Boolean) {
        if (interact) {
            mc.netHandler.addToSendQueue(C02PacketUseEntity(interactEntity, interactEntity.positionVector))
            mc.netHandler.addToSendQueue(C02PacketUseEntity(interactEntity, C02PacketUseEntity.Action.INTERACT))
        }
        when (AutoBlockModeValue.get().toLowerCase()) {
            "packet" -> {
                mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
                blockingStatus = true
            }
            "vanilla" -> {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld,
                                mc.thePlayer.inventory.getCurrentItem())) {
                    mc.getItemRenderer().resetEquippedProgress2();
                }
                blockingStatus = true
            }
        }
    }


    /**
     * Stop blocking
     */
    private fun stopBlocking() {
        if (blockingStatus) {
            mc.thePlayer.itemInUseCount = 0
            when (AutoBlockModeValue.get().toLowerCase()) {
                "packet" -> {
                    mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
                }
                "vanilla" -> {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false)
                    mc.playerController.onStoppedUsingItem(mc.thePlayer)
                }
            }
            blockingStatus = false
        }
    }

    /**
     * Check if run should be cancelled
     */
    private val cancelRun: Boolean
        get() = mc.thePlayer.isSpectator || !isAlive(mc.thePlayer)
                || HDebug.moduleManager[Blink::class.java]!!.state || HDebug.moduleManager[FreeCam::class.java]!!.state

    /**
     * Check if [entity] is alive
     */
    private fun isAlive(entity: EntityLivingBase) = entity.isEntityAlive && entity.health > 0 || HYTValue.get() && entity.hurtTime > 3 || Rede2Value.get() && entity.hurtTime > 3


    /**
     * Check if player is able to block
     */
    private val canBlock: Boolean
        get() = mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword

    /**
     * Range
     */
    private val maxRange: Float
        get() = max(rangeValue.get(), throughWallsRangeValue.get())

    private fun getRange(entity: Entity) =
            (if (mc.thePlayer.getDistanceToEntityBox(entity) >= throughWallsRangeValue.get()) rangeValue.get() else throughWallsRangeValue.get()) - if (mc.thePlayer.isSprinting) rangeSprintReducementValue.get() else 0F

    /**
     * HUD Tag
     */
    override val tag: String?
        get() = (if (HYTValue.get()) targetModeValue.get() + " HuaYuTing" else (if (Rede2Value.get()) targetModeValue.get() + " RedeSky" else (if (aacValue.get()) targetModeValue.get() + " AAC" else targetModeValue.get() + " Normal")))
}