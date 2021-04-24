package org.me.ByBlueHeart.HDebugClient.Modules.Render;

import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.UpdateEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "AntiObsidian", description = "Anti Obsidian.", category = ModuleCategory.RENDER)
public class AntiObs extends Module {

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        BlockPos sand = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 3.0D, mc.thePlayer.posZ));
        Block sandblock = mc.theWorld.getBlockState(sand).getBlock();
        BlockPos forge = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 2.0D, mc.thePlayer.posZ));
        Block forgeblock = mc.theWorld.getBlockState(forge).getBlock();
        BlockPos obsidianpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ));
        Block obsidianblock = mc.theWorld.getBlockState(obsidianpos).getBlock();

        if (obsidianblock == Block.getBlockById(49)) {
            bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(),
                    mc.objectMouseOver.getBlockPos().getZ());
            BlockPos downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ));
            mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
        }
        if (forgeblock == Block.getBlockById(61)) {
            bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(),
                    mc.objectMouseOver.getBlockPos().getZ());
            BlockPos downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ));
            mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
        }
        if (sandblock == Block.getBlockById(12) || sandblock == Block.getBlockById(13)) {
            bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(),
                    mc.objectMouseOver.getBlockPos().getZ());
            BlockPos downpos = new BlockPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 3.0D, mc.thePlayer.posZ));
            mc.thePlayer.addChatMessage(new ChatComponentText("你头上有个沙子,照顾好它 :D"));
            mc.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
        }
    }

    public void bestTool(int x, int y, int z) {
        int blockId = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
        int bestSlot = 0;
        float f = -1.0F;
        for (int i1 = 36; i1 < 45; i1++) {
            try {
                ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
                if ((curSlot.getItem() instanceof net.minecraft.item.ItemTool || curSlot.getItem() instanceof net.minecraft.item.ItemSword ||
                        curSlot.getItem() instanceof net.minecraft.item.ItemShears) &&
                        curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f) {
                    bestSlot = i1 - 36;
                    f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
                }
                } catch (Exception exception) {}
        }

        if (f != -1.0F) {
            mc.thePlayer.inventory.currentItem = bestSlot;
            mc.playerController.updateController();
        }
    }
}