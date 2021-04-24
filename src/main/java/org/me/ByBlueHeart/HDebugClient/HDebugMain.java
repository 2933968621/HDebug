package org.me.ByBlueHeart.HDebugClient;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.utils.ClientUtils;
import org.lwjgl.opengl.Display;

public class HDebugMain {
    public static boolean BuyFrameOfMind = false;
    public static boolean BuyBodacious = false;
    public static boolean BuyEyeDragon = false;
    public static boolean BuyBlackHuman = false;
    public static Boolean nice = false;
    public static String NMSL = "Storm NMSL";
    public HDebug hdebug;

    public HDebugMain(HDebug hdebug){
        this.hdebug = hdebug;
    }

    public void onStarting(){
        Display.setTitle("HDebug For LiquidBounce b"+HDebug.CLIENT_VERSION+" By BlueHeart | Minecraft " + HDebug.MINECRAFT_VERSION + " | 正在启动......");
        ClientUtils.getLogger().info("正在启动");
    }

    public void onStarted(){
        Display.setTitle("HDebug For LiquidBounce b"+HDebug.CLIENT_VERSION+" By BlueHeart | Minecraft " + HDebug.MINECRAFT_VERSION + " | 我愿意化作天中的一颗明星来守护你 / Reborn Build");
        ClientUtils.getLogger().info("加载完成 欢迎使用HDebug b"+HDebug.CLIENT_VERSION+" Code By BlueHeart");
    }
}