package net.blueheart.hdebug.features.module

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.KeyEvent
import net.blueheart.hdebug.event.Listenable
import net.blueheart.hdebug.features.module.modules.`fun`.Derp
import net.blueheart.hdebug.features.module.modules.`fun`.SkinDerp
import net.blueheart.hdebug.features.module.modules.combat.*
import net.blueheart.hdebug.features.module.modules.exploit.*
import net.blueheart.hdebug.features.module.modules.misc.*
import net.blueheart.hdebug.features.module.modules.movement.*
import net.blueheart.hdebug.features.module.modules.player.*
import net.blueheart.hdebug.features.module.modules.render.*
import net.blueheart.hdebug.features.module.modules.world.*
import net.blueheart.hdebug.features.module.modules.world.Timer
import net.blueheart.hdebug.utils.ClientUtils
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.*
import org.me.ByBlueHeart.HDebugClient.Modules.Exploit.*
import org.me.ByBlueHeart.HDebugClient.Modules.Fun.AutoWatchDog
import org.me.ByBlueHeart.HDebugClient.Modules.Fun.ByBlueHeart
import org.me.ByBlueHeart.HDebugClient.Modules.Fun.ZiHui
import org.me.ByBlueHeart.HDebugClient.Modules.Misc.*
import org.me.ByBlueHeart.HDebugClient.Modules.Movement.*
import org.me.ByBlueHeart.HDebugClient.Modules.Player.*
import org.me.ByBlueHeart.HDebugClient.Modules.Render.*
import org.me.ByBlueHeart.HDebugClient.Modules.World.*
import java.util.*

class ModuleManager : Listenable {

    val modules = TreeSet<Module> { module1, module2 -> module1.name.compareTo(module2.name) }
    private val moduleClassMap = hashMapOf<Class<*>, Module>()

    init {
        HDebug.eventManager.registerListener(this)
    }

    /**
     * Register all modules
     */
    fun registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...")

        registerModules(
                AutoArmor::class.java,
                AutoBow::class.java,
                AutoLeave::class.java,
                AutoPot::class.java,
                AutoSoup::class.java,
                BowAimbot::class.java,
                Trigger::class.java,
                AntiKnockBack::class.java,
                Flight::class.java,
                ClickGUI::class.java,
                HighJump::class.java,
                InvWalk::class.java,
                SafeWalk::class.java,
                WallClimb::class.java,
                Strafe::class.java,
                Sprint::class.java,
                Teams::class.java,
                NoRotateSet::class.java,
                CivBreak::class.java,
                SpeedMine::class.java,
                FastPlace::class.java,
                Speed::class.java,
                Tracers::class.java,
                FastEat::class.java,
                Teleport::class.java,
                Fullbright::class.java,
                ItemESP::class.java,
                StorageESP::class.java,
                Projectiles::class.java,
                NoClip::class.java,
                FastClimb::class.java,
                Step::class.java,
                AutoRespawn::class.java,
                AutoTool::class.java,
                NoWeb::class.java,
                Spammer::class.java,
                IceSpeed::class.java,
                Zoot::class.java,
                Regen::class.java,
                NoFall::class.java,
                Blink::class.java,
                NameProtect::class.java,
                NoHurtCam::class.java,
                Ghost::class.java,
                MCF::class.java,
                XRay::class.java,
                Timer::class.java,
                Sneak::class.java,
                SkinDerp::class.java,
                Paralyze::class.java,
                GhostHand::class.java,
                AutoWalk::class.java,
                AutoBreak::class.java,
                FreeCam::class.java,
                Aimbot::class.java,
                Eagle::class.java,
                HitBox::class.java,
                AntiCactus::class.java,
                Plugins::class.java,
                AntiHunger::class.java,
                ConsoleSpammer::class.java,
                LongJump::class.java,
                Parkour::class.java,
                LadderJump::class.java,
                FastBow::class.java,
                MultiActions::class.java,
                AirJump::class.java,
                AutoClicker::class.java,
                NoBob::class.java,
                BlockOverlay::class.java,
                NoFriends::class.java,
                BlockESP::class.java,
                Clip::class.java,
                Phase::class.java,
                ServerCrasher::class.java,
                NoFOV::class.java,
                FastStairs::class.java,
                SwingAnimation::class.java,
                Derp::class.java,
                ReverseStep::class.java,
                TNTBlock::class.java,
                TrueSight::class.java,
                IRC::class.java,
                AntiBlind::class.java,
                NoSwing::class.java,
                BedGodMode::class.java,
                BugUp::class.java,
                Breadcrumbs::class.java,
                AbortBreaking::class.java,
                PotionSaver::class.java,
                CameraClip::class.java,
                WaterSpeed::class.java,
                Ignite::class.java,
                SlimeJump::class.java,
                MoreCarry::class.java,
                NoPitchLimit::class.java,
                Kick::class.java,
                Liquids::class.java,
                AtAllProvider::class.java,
                AirLadder::class.java,
                GodMode::class.java,
                TPHit::class.java,
                ForceUnicodeChat::class.java,
                ItemTeleport::class.java,
                BufferSpeed::class.java,
                SuperKnockback::class.java,
                ProphuntESP::class.java,
                AutoFish::class.java,
                Damage::class.java,
                Freeze::class.java,
                KeepContainer::class.java,
                VehicleOneHit::class.java,
                Reach::class.java,
                Rotations::class.java,
                NoJumpDelay::class.java,
                BlockWalk::class.java,
                AntiAFK::class.java,
                PerfectHorseJump::class.java,
                HUD::class.java,
                TNTESP::class.java,
                ComponentOnHover::class.java,
                KeepAlive::class.java,
                ResourcePackSpoof::class.java,
                NoSlowBreak::class.java,
                PortalMenu::class.java,
                AutoSword::class.java,
                Criticals::class.java,
                Aura::class.java,
                NoSlowDown::class.java,
                AntiBot::class.java,
                ChestStealer::class.java,
                BlockFly::class.java,
                Tower::class.java,
                ESP::class.java,
                NameTags::class.java,
                Chams::class.java,
                InvManager::class.java,
                AntiFall::class.java,
                AutoGG::class.java,
                AutoL::class.java,
                ModCheck::class.java,
                PacketMotior::class.java,
                AAC437Helper::class.java,
                AirLongJump::class.java,
                AirHighJump::class.java,
                TargetHUD::class.java,
                MemoryFix::class.java,
                AntiFireBall::class.java,
                AntiObs::class.java,
                ZiHui::class.java,
                AutoBed::class.java,
                AutoSay::class.java,
                AutoCounterAttack::class.java,
                StartAutoSay::class.java,
                Animation::class.java,
                DMGParticle::class.java,
                WorldTime::class.java,
                JoinSay::class.java,
                NewTargetHUD::class.java,
                AntiSpammer::class.java,
                DeathPrompts::class.java,
                AntiAntiGodPlayer::class.java,
                AutoReJoin::class.java,
                HytBypass::class.java,
                AutoTeam::class.java,
                ByBlueHeart::class.java,
                TPAura::class.java,
                Nuker::class.java,
                FakeLag::class.java,
                BlockESP::class.java,
                TargetStrafe::class.java,
                KeepSprint::class.java,
                WTap::class.java,
                AntiFire::class.java,
                ShowHWID::class.java,
                Jesus::class.java,
                AutoWatchDog::class.java,
                Head::class.java,
                AutoGApple::class.java,
                Give::class.java,
                AutoMid::class.java,
                PacketFlight::class.java,
                HypixelBlockFly::class.java,
                AutoDisabler::class.java,
                Keyrender::class.java,
                Scaffold::class.java,
                ArmorBreaker::class.java,
                Disabler::class.java,
                FlagDetector::class.java,
                AutoJump::class.java
        )

        registerModule(NoScoreboard)
        registerModule(Fucker)
        registerModule(ChestAura)

        ClientUtils.getLogger().info("[ModuleManager] Loaded ${modules.size} modules.")
    }


    /**
     * Register [module]
     */
    fun registerModule(module: Module) {
        modules += module
        moduleClassMap[module.javaClass] = module

        generateCommand(module)
        HDebug.eventManager.registerListener(module)
    }

    /**
     * Register [moduleClass]
     */
    private fun registerModule(moduleClass: Class<out Module>) {
        try {
            registerModule(moduleClass.newInstance())
        } catch (e: Throwable) {
            ClientUtils.getLogger().error("Failed to load module: ${moduleClass.name} (${e.javaClass.name}: ${e.message})")
        }
    }

    /**
     * Register a list of modules
     */
    @SafeVarargs
    fun registerModules(vararg modules: Class<out Module>) {
        modules.forEach(this::registerModule)
    }

    /**
     * Unregister module
     */
    fun unregisterModule(module: Module) {
        modules.remove(module)
        moduleClassMap.remove(module::class.java)
        HDebug.eventManager.unregisterListener(module)
    }

    /**
     * Generate command for [module]
     */
    private fun generateCommand(module: Module) {
        val values = module.values

        if (values.isEmpty())
            return

        HDebug.commandManager.registerCommand(ModuleCommand(module, values))
    }

    /**
      Legacy stuff

      TODO: Remove later when everything is translated to Kotlin
     */

    /**
     * Get module by [moduleClass]
     */
    fun getModule(moduleClass: Class<*>) = moduleClassMap[moduleClass]

    operator fun get(clazz: Class<*>) = getModule(clazz)

    /**
     * Get module by [moduleName]
     */
    fun getModule(moduleName: String?) = modules.find { it.name.equals(moduleName, ignoreCase = true) }

    /**
     * Module related events
     */

    /**
     * Handle incoming key presses
     */
    @EventTarget
    private fun onKey(event: KeyEvent) = modules.filter { it.keyBind == event.key }.forEach { it.toggle() }

    override fun handleEvents() = true
}
