package org.me.ByBlueHeart.HDebugClient.Modules.Combat

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.JumpEvent
import net.blueheart.hdebug.event.PacketEvent
import net.blueheart.hdebug.event.UpdateEvent
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.features.module.modules.movement.Speed
import net.blueheart.hdebug.utils.timer.MSTimer
import net.blueheart.hdebug.value.BoolValue
import net.blueheart.hdebug.value.FloatValue
import net.blueheart.hdebug.value.ListValue
import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minecraft.network.play.server.S27PacketExplosion
import net.minecraft.util.MathHelper
import org.me.ByBlueHeart.HDebugClient.Utils.MovementUtils

@ModuleInfo(name = "AntiKnockBack", description = "Allows you to modify the amount of knockback you take.", category = ModuleCategory.COMBAT)
class AntiKnockBack : Module() {

    /**
     * OPTIONS
     */
    private val horizontalValue = FloatValue("Horizontal", 0F, 0F, 1F)
    private val verticalValue = FloatValue("Vertical", 0F, 0F, 1F)
    private val modeValue = ListValue("Mode", arrayOf("Normal", "0%", "AAC", "AACPush", "AACZero","AAC4.1.0","AACLatest","AACLow",
            "Reverse", "SmoothReverse","SmoothAAC", "Jump", "Glitch"), "Normal")

    // Bypass
    private val GroudValue = BoolValue("Ground", true)

    // Reverse
    private val reverseStrengthValue = FloatValue("ReverseStrength", 1F, 0.1F, 1F)
    private val reverse2StrengthValue = FloatValue("SmoothReverseStrength", 0.05F, 0.02F, 0.1F)

    // AAC
    private val aacSmoothStrengthValue = FloatValue("AACSmoothStrength", 0.03f, 0.02f, 0.04f)

    // AAC Push
    private val aacPushXZReducerValue = FloatValue("AACPushXZReducer", 2F, 1F, 3F)
    private val aacPushYReducerValue = BoolValue("AACPushYReducer", true)

    /**
     * VALUES
     */
    private var velocityTimer = MSTimer()
    private var velocityInput = false

    // SmoothAAC
    private var aacsmoothHurt: Boolean = false

    // SmoothReverse
    private var reverseHurt = false

    // AACPush
    private var jump = false

    override val tag: String?
        get() = (if (modeValue.get() == "Normal") modeValue.get() + " H:" + horizontalValue.get() + " V:" + verticalValue.get() else modeValue.get())

    override fun onDisable() {
        mc.thePlayer?.speedInAir = 0.02F
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.isInWater || mc.thePlayer.isInLava || mc.thePlayer.isInWeb)
            return
        if(!mc.thePlayer.onGround && GroudValue.get())
            return

        when (modeValue.get().toLowerCase()) {
            "jump" -> if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0.42

                val yaw = mc.thePlayer.rotationYaw * 0.017453292F
                mc.thePlayer.motionX -= MathHelper.sin(yaw) * 0.2
                mc.thePlayer.motionZ += MathHelper.cos(yaw) * 0.2
            }

            "glitch" -> {
                mc.thePlayer.noClip = velocityInput
                if (mc.thePlayer.hurtTime == 7)
                    mc.thePlayer.motionY = 0.4

                velocityInput = false
            }

            "reverse" -> {
                if (!velocityInput)
                    return

                if (!mc.thePlayer.onGround) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * reverseStrengthValue.get())
                } else if (velocityTimer.hasTimePassed(80L))
                    velocityInput = false
            }

            "smoothreverse" -> {
                if (!velocityInput) {
                    mc.thePlayer.speedInAir = 0.02F
                    return
                }

                if (mc.thePlayer.hurtTime > 0)
                    reverseHurt = true

                if (!mc.thePlayer.onGround) {
                    if (reverseHurt)
                        mc.thePlayer.speedInAir = reverse2StrengthValue.get()
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false
                    reverseHurt = false
                }
            }

            "aac" -> if (velocityInput && velocityTimer.hasTimePassed(80L)) {
                mc.thePlayer.motionX *= horizontalValue.get()
                mc.thePlayer.motionZ *= horizontalValue.get()
                mc.thePlayer.motionY *= verticalValue.get()
                velocityInput = false
            }

            "aac4.1.0" -> if (mc.thePlayer.hurtTime > 0) {
                mc.thePlayer.onGround = true;
                velocityInput = false
            }

            "aaclatest" -> if (this.velocityInput && this.velocityTimer.hasTimePassed(50L)) {
                mc.thePlayer.motionX *= horizontalValue.get()
                mc.thePlayer.motionZ *= horizontalValue.get()
                mc.thePlayer.motionY *= verticalValue.get()
                velocityInput = false
            }

            "aaclow" -> if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime <= 5) {
                mc.thePlayer.motionX *= 0.53333
                mc.thePlayer.motionZ *= 0.53333
            }

            "smoothaac" -> {
                if (!velocityInput) {
                    mc.thePlayer.speedInAir = 0.02F
                    return;
                }
                if (mc.thePlayer.hurtTime > 0)
                aacsmoothHurt = true;
                if (!mc.thePlayer.onGround) {
                    if (this.reverseHurt) {
                        mc.thePlayer.speedInAir = (aacSmoothStrengthValue.get())
                    }
                }
                if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false;
                    aacsmoothHurt = false;
                }
            }

            "aacpush" -> {
                if (jump) {
                    if (mc.thePlayer.onGround)
                        jump = false
                } else {
                    // Strafe
                    if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.motionX != 0.0 && mc.thePlayer.motionZ != 0.0)
                        mc.thePlayer.onGround = true

                    // Reduce Y
                    if (mc.thePlayer.hurtResistantTime > 0 && aacPushYReducerValue.get()
                            && !HDebug.moduleManager[Speed::class.java]!!.state)
                        mc.thePlayer.motionY -= 0.014999993
                }

                // Reduce XZ
                if (mc.thePlayer.hurtResistantTime >= 19) {
                    val reduce = aacPushXZReducerValue.get()

                    mc.thePlayer.motionX /= reduce
                    mc.thePlayer.motionZ /= reduce
                }
            }

            "aaczero" -> if (mc.thePlayer.hurtTime > 0) {
                if (!velocityInput || mc.thePlayer.onGround || mc.thePlayer.fallDistance > 2F)
                    return

                mc.thePlayer.addVelocity(0.0, -1.0, 0.0)
                mc.thePlayer.onGround = true
            } else
                velocityInput = false
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S12PacketEntityVelocity) {
            if (mc.thePlayer == null || (mc.theWorld?.getEntityByID(packet.entityID) ?: return) != mc.thePlayer)
                return

            velocityTimer.reset()

            when (modeValue.get().toLowerCase()) {
                "normal" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == 0F && vertical == 0F)
                        event.cancelEvent()

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }

                "0%" -> {
                    event.cancelEvent()
                }

                "aac", "reverse", "smoothreverse", "aaczero" -> velocityInput = true

                "glitch" -> {
                    if (!mc.thePlayer.onGround)
                        return

                    velocityInput = true
                    event.cancelEvent()
                }
            }
        }

        if (packet is S27PacketExplosion) {
            // TODO: Support velocity for explosions
            event.cancelEvent()
        }
    }

    @EventTarget
    fun onJump(event: JumpEvent) {
        if (mc.thePlayer == null || mc.thePlayer.isInWater || mc.thePlayer.isInLava || mc.thePlayer.isInWeb)
            return

        when (modeValue.get().toLowerCase()) {
            "aacpush" -> {
                jump = true

                if (!mc.thePlayer.isCollidedVertically)
                    event.cancelEvent()
            }
            "aaczero" -> if (mc.thePlayer.hurtTime > 0)
                event.cancelEvent()
        }
    }
}