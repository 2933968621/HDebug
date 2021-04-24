package org.me.ByBlueHeart.HDebugClient.Modules.Misc

import net.blueheart.hdebug.features.module.Module
import net.blueheart.hdebug.features.module.ModuleCategory
import net.blueheart.hdebug.features.module.ModuleInfo
import net.blueheart.hdebug.value.BoolValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor

@ModuleInfo(name = "Teams", description = "Prevents Aura from attacking team mates.", category = ModuleCategory.MISC)
class Teams : Module() {
    private val scoreboardValue = BoolValue("Scoreboard", true)
    private val colorValue = BoolValue("Color", true)
    private val gommeSWValue = BoolValue("GommeSW", false)
    private val armorValue = BoolValue("Armor", false)

    /**
     * Check if [entity] is in your own team using scoreboard, name color or team prefix
     */
    fun isInYourTeam(entity: EntityLivingBase): Boolean {
        mc.thePlayer ?: return false

        if (scoreboardValue.get() && mc.thePlayer.team != null && entity.team != null &&
                mc.thePlayer.team.isSameTeam(entity.team))
            return true

        if (gommeSWValue.get() && mc.thePlayer.displayName != null && entity.displayName != null) {
            val targetName = entity.displayName.formattedText.replace("§r", "")
            val clientName = mc.thePlayer.displayName.formattedText.replace("§r", "")
            if (targetName.startsWith("T") && clientName.startsWith("T"))
                if (targetName[1].isDigit() && clientName[1].isDigit())
                    return targetName[1] == clientName[1]
        }

        if(armorValue.get()){
            val entityPlayer = entity as EntityPlayer
            if(mc.thePlayer.inventory.armorInventory[3] != null && entityPlayer.inventory.armorInventory[3] != null){
                val myHead = mc.thePlayer.inventory.armorInventory[3]
                val myItemArmor = myHead.item as ItemArmor


                val entityHead = entityPlayer.inventory.armorInventory[3]
                var entityItemArmor = myHead.item as ItemArmor

                if(myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead)){
                    return true
                }
            }
        }

        if (colorValue.get() && mc.thePlayer.displayName != null && entity.displayName != null) {
            val targetName = entity.displayName.formattedText.replace("§r", "")
            val clientName = mc.thePlayer.displayName.formattedText.replace("§r", "")
            return targetName.startsWith("§${clientName[1]}")
        }

        return false
    }
}