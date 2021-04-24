/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.blueheart.hdebug.script.api.module

import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.value.Value

/**
 * A adapted module
 *
 * Script Support
 * @author CCBlueX
 */
class AdaptedModule(private val module : Module) {

    /**
     * @return module name of adapted module
     */
    fun getName() : String =  module.name

    /**
     * @return module description of adapted module
     */
    fun getDescription() : String = module.description

    /**
     * @return module category of adapted module
     */
    fun getCategory() : String = module.category.displayName

    /**
     * @return state of adapted module
     */
    fun getState() : Boolean = module.state

    /**
     * Set state of adapted module
     *
     * @param state New state for the module
     */
    fun setState(state : Boolean) {
        module.state = state
    }

    /**
     * @return keybind of adapted module
     */
    fun getBind() : Int =  module.keyBind

    /**
     * Set keybind of adapted module
     *
     * @param key New keybind for the adapted module
     */
    fun setBind(key : Int) {
        module.keyBind = key
    }

    /**
     * Gets a value from the module
     *
     * @param name Name of the Value
     */
    fun getValue(name: String): Any = AdaptedValue(module.getValue(name) as Value<Any>)

    /**
     * Register adapted module
     */
    fun register() {
        HDebug.moduleManager.registerModule(module)
    }

    /**
     * Unregiser adapted module
     */
    fun unregister() {
        HDebug.moduleManager.unregisterModule(module)
    }
}