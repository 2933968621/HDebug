/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.blueheart.hdebug.script.api

import jdk.nashorn.api.scripting.ScriptObjectMirror
import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.script.api.module.AdaptedModule
import net.blueheart.hdebug.script.api.module.ScriptModule

/**
 * A script api class for module support
 *
 * @author CCBlueX
 */
object ModuleManager {

    /**
     * Register a java script based module
     *
     * @param scriptObjectMirror Java Script function for module (like a module class)
     */
    @JvmStatic
    fun registerModule(scriptObjectMirror: ScriptObjectMirror): Module {
        val module = ScriptModule(scriptObjectMirror)
        HDebug.moduleManager.registerModule(module)
        return module
    }

    /**
     * Unregister a module
     *
     * @param module Instance of target module
     */
    @JvmStatic
    fun unregisterModule(module : Module) {
        unregisterModule(module, true)
    }

    /**
     * Unregister a module
     *
     * @param scriptObjectMirror Java Script function for module (like a module class)
     */
    @JvmStatic
    fun unregisterModule(scriptObjectMirror : ScriptObjectMirror) {
        val module = ScriptModule(scriptObjectMirror)

        unregisterModule(HDebug.moduleManager.getModule(module.name)!!, true)
    }

    /**
     * Unregister a module
     *
     * @param module Instance of target module
     * @param autoDisable Automatically disable module when unregistering module
     */
    @JvmStatic
    fun unregisterModule(module : Module, autoDisable : Boolean) {
        if(autoDisable && module.state) module.state = false

        HDebug.moduleManager.unregisterModule(module)
    }

    /**
     * Search for module name and return then a adapted module
     *
     * @param moduleName Name of module
     */
    @JvmStatic
    fun getModule(moduleName: String) = AdaptedModule(HDebug.moduleManager.getModule(moduleName)!!)

    /**
     * @return a list of all modules
     */
    @JvmStatic
    fun getModules() = HDebug.moduleManager.modules.map { AdaptedModule(it) }
}